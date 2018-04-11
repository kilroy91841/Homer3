package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.ITransactionRepository;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.external.common.espn.ESPNTransaction;
import com.homer.external.common.espn.IESPNClient;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.type.*;
import com.homer.util.core.$;
import com.homer.util.core.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.homer.service.utility.ESPNUtility.translateESPNPosition;
import static com.homer.service.utility.ESPNUtility.translateESPNTransactionType;

/**
 * Created by arigolub on 7/25/16.
 */
public class TransactionService extends BaseIdService<Transaction> implements ITransactionService {

    final static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private ITransactionRepository transactionRepo;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IESPNClient espnClient;
    private IEmailService emailService;
    private IUserService userService;
    private ITeamService teamService;

    public TransactionService(ITransactionRepository transactionRepo,
                              IPlayerService playerService,
                              IPlayerSeasonService playerSeasonService,
                              IESPNClient espnClient,
                              IEmailService emailService,
                              IUserService userService,
                              ITeamService teamService) {
        super(transactionRepo);
        this.transactionRepo = transactionRepo;
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.espnClient = espnClient;
        this.emailService = emailService;
        this.userService = userService;
        this.teamService = teamService;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepo.getAll();
    }

    @Override
    public boolean doesTransactionExist(Transaction transaction) {
        List<Transaction> transactions = transactionRepo.getByText(transaction.getText());
        return $.of(transactions).anyMatch(t -> t.getTransactionDate().equals(transaction.getTransactionDate()));
    }

    private String getToday()
    {
        DateTime now = DateTime.now().minusHours(8);
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        return "" + year + monthStr + dayStr;
    }

    private List<Transaction> getTransactions(String date)
    {
        logger.info("Getting transactions for " + date);
        List<Transaction> allTransactions = Lists.newArrayList();
        List<ESPNTransaction> espnTransactions = Lists.newArrayList();
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.ADD, date, date));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.DROP, date, date));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.MOVE, date, date));
        Pair<List<Transaction>, List<ESPNTransaction>> pair = translateESPNTransactions(espnTransactions);
        allTransactions.addAll(pair.getFirst());

        handleErrorTransactions("Error parsing ESPN transactions: ESPN players not in Homer", $.of(pair.getSecond()).toList(ESPNTransaction::getText));

        return $.of(allTransactions).sorted();
    }

    @Override
    public List<Transaction> processDailyTransactions() {
        String today = getToday();
        return processTransactions(today, true);
    }

    @Override
    public List<Transaction> processTransactions(String date, boolean commitAndEmail)
    {
        List<Transaction> transactions = getTransactions(date);
        List<Transaction> createdTransactions = Lists.newArrayList();
        List<String> erroredTransactions = Lists.newArrayList();
        for (Transaction t : transactions) {
            try {
                if (doesTransactionExist(t)) {
                    logger.info("Already encountered transaction: " + t);
                    continue;
                }
                PlayerSeason playerSeason = playerSeasonService.getCurrentPlayerSeason(t.getPlayerId());
                checkNotNull(playerSeason);
                if (t.getTransactionType() == TransactionType.ADD) {
                    if (playerSeason.getTeamId() != null && playerSeason.getTeamId() != t.getTeamId())
                    {
                        if (playerSeason.getFantasyPosition() == Position.MINORLEAGUES)
                        {
                            handleIllegalMinorLeagueAdd(playerSeason, t, commitAndEmail);
                        }
                        throw new IllegalArgumentException(String.format("%s was added to %s but is on %s", playerSeason.getPlayerId(), t.getTeamId(), playerSeason.getTeamId()));
                    }
                    PlayerElf.switchTeam(playerSeason, t.getTeamId());
                    PlayerElf.switchFantasyPosition(playerSeason, playerSeason.getFantasyPosition(), Position.BENCH);
                } else if (t.getTransactionType() == TransactionType.DROP) {
                    if (!Objects.equals(playerSeason.getTeamId(), t.getTeamId()))
                    {
                        throw new IllegalArgumentException("Supplied old team does not match existing team");
                    }
                    PlayerElf.switchTeam(playerSeason, null);
                } else if (t.getTransactionType() == TransactionType.MOVE) {
                    PlayerElf.switchFantasyPosition(playerSeason, playerSeason.getFantasyPosition(), t.getNewPosition());
                }
                checkNotNull(playerSeason, "PlayerSeason was null after applying transaction");
                playerSeasonService.upsert(playerSeason);
                if (commitAndEmail)
                {
                    Transaction createdTransaction = upsert(t);
                    checkNotNull(createdTransaction, "Attempt to save transaction returned null for transaction");
                    createdTransactions.add(createdTransaction);
                }
                else
                {
                    createdTransactions.add(t);
                }
            } catch (Exception e) {
                logger.error("Encountered an error processing transaction: " + t, e);
                erroredTransactions.add(t.getText() + ": " + e.getMessage());
            }
        }
        if (commitAndEmail)
        {
            handleErrorTransactions("Errors processing the following transactions", erroredTransactions);
        }
        return createdTransactions;
    }

    private Pair<List<Transaction>, List<ESPNTransaction>> translateESPNTransactions(List<ESPNTransaction> espnTransactions) {
        if (espnTransactions.size() == 0) {
            return new Pair(Lists.newArrayList(), Lists.newArrayList());
        }
        List<Transaction> transactions = Lists.newArrayList();
        List<ESPNTransaction> errorTransactions = Lists.newArrayList();
        List<String> playerNames = $.of(espnTransactions).toList(espnTrans -> espnTrans.getPlayerName().replace("*", ""));
        Map<String, Player> playersByName = $.of(playerService.getPlayersByNames(playerNames)).toMap(Player::getName);
        Map<String, Player> playersByEspnName = $.of(playerService.getPlayersByEspnNames(playerNames)).toMap(Player::getEspnName);
        for (ESPNTransaction espnTrans : espnTransactions) {
            Player player = playersByName.get(espnTrans.getPlayerName());
            if (player == null)
            {
                player = playersByEspnName.get(espnTrans.getPlayerName());
            }
            if (player == null) {
                errorTransactions.add(espnTrans);
                continue;
            }
            long playerId = player.getId();
            Transaction transaction = new Transaction();
            transaction.setText(espnTrans.getText());
            transaction.setNewPosition(translateESPNPosition(espnTrans.getNewPosition()));
            transaction.setOldPosition(translateESPNPosition(espnTrans.getOldPosition()));
            transaction.setPlayerId(playerId);
            transaction.setTransactionDate(espnTrans.getTransDate());
            transaction.setTransactionType(translateESPNTransactionType(espnTrans.getType()));
            transaction.setTeamId(espnTrans.getTeamId());
            transactions.add(transaction);
        }
        return new Pair(transactions, errorTransactions);
    }

    private void handleErrorTransactions(String subject, List<String> transactions) {
        if (transactions.size() == 0) {
            return;
        }

        HtmlObject htmlObj = HtmlObject.of(HtmlTag.DIV);

        for (String transaction : transactions) {
            logger.error("Encountered a transaction that had an error: " + transaction);
            htmlObj.child(HtmlObject.of(HtmlTag.P).body(transaction));
        }

        EmailRequest emailRequest = new EmailRequest(Lists.newArrayList(IEmailService.COMMISSIONER_EMAIL), subject, htmlObj);
        emailService.sendEmail(emailRequest);
    }

    private void handleIllegalMinorLeagueAdd(PlayerSeason playerSeason, Transaction transaction, boolean commitAndEmail)
    {
        Player player = checkNotNull(playerService.getById(playerSeason.getPlayerId()));
        List<Long> teamIds = Lists.newArrayList(transaction.getTeamId(), playerSeason.getTeamId());
        Map<Long, Team> teamMap = $.of(teamService.getByIds(teamIds)).toMap(Team::getId);
        List<User> users = userService.getUsersForTeams(teamIds);
        String offendingTeamName = teamMap.get(transaction.getTeamId()).getName();
        String subject = String.format("Illegal add of %s by %s", player.getName(), offendingTeamName);
        String body = String.format("%s cannot add %s because that player is on %s's minor league roster. %s should drop the player on ESPN and Ari will refund the transaction.",
                offendingTeamName, player.getName(), teamMap.get(playerSeason.getTeamId()).getName(), offendingTeamName);
        HtmlObject htmlObj = HtmlObject.of(HtmlTag.DIV);
        htmlObj.child(HtmlObject.of(HtmlTag.P).body(body));
        List<String> emails = $.of(users).toList(User::getEmail);
        emails.add(IEmailService.COMMISSIONER_EMAIL);
        if (commitAndEmail) {
            EmailRequest emailRequest = new EmailRequest(emails, subject, htmlObj);
            emailService.sendEmail(emailRequest);
            upsert(transaction);
        }
    }
}
