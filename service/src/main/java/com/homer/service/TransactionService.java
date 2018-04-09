package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.ITransactionRepository;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.external.common.espn.ESPNTransaction;
import com.homer.external.common.espn.IESPNClient;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.homer.util.core.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
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

    public TransactionService(ITransactionRepository transactionRepo,
                              IPlayerService playerService,
                              IPlayerSeasonService playerSeasonService,
                              IESPNClient espnClient,
                              IEmailService emailService) {
        super(transactionRepo);
        this.transactionRepo = transactionRepo;
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.espnClient = espnClient;
        this.emailService = emailService;
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

    @Override
    public List<Transaction> getDailyTransactions() {
        DateTime now = DateTime.now().minusHours(8);
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        String today = "" + year + monthStr + dayStr;
        logger.info("Getting transactions for " + today);
        List<Transaction> allTransactions = Lists.newArrayList();
        List<ESPNTransaction> espnTransactions = Lists.newArrayList();
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.ADD, today, today));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.DROP, today, today));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.MOVE, today, today));
        Pair<List<Transaction>, List<ESPNTransaction>> pair = translateESPNTransactions(espnTransactions);
        allTransactions.addAll(pair.getFirst());

        handleErrorTransactions("Error parsing ESPN transactions: ESPN players not in Homer", $.of(pair.getSecond()).toList(ESPNTransaction::getText));

        return $.of(allTransactions).sorted();
    }

    @Override
    public List<Transaction> processDailyTransactions() {
        List<Transaction> transactions = getDailyTransactions();
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
                Transaction createdTransaction = upsert(t);
                checkNotNull(createdTransaction, "Attempt to save transaction returned null for transaction");
                createdTransactions.add(createdTransaction);
            } catch (Exception e) {
                logger.error("Encountered an error processing transaction: " + t, e);
                erroredTransactions.add(t.getText() + ": " + e.getMessage());
            }
        }
        handleErrorTransactions("Errors processing the following transactions", erroredTransactions);
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
        for (ESPNTransaction espnTrans : espnTransactions) {
            if (!playersByName.containsKey(espnTrans.getPlayerName())) {
                errorTransactions.add(espnTrans);
                continue;
            }
            long playerId = playersByName.get(espnTrans.getPlayerName()).getId();
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
}
