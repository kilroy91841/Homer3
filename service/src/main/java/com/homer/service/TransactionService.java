package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.ITransactionRepository;
import com.homer.external.common.espn.ESPNTransaction;
import com.homer.external.common.espn.IESPNClient;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.sun.tools.javac.util.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by arigolub on 7/25/16.
 */
public class TransactionService extends BaseIdService<Transaction> implements ITransactionService {

    final static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private ITransactionRepository transactionRepo;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IESPNClient espnClient;

    public TransactionService(ITransactionRepository transactionRepo,
                              IPlayerService playerService,
                              IPlayerSeasonService playerSeasonService,
                              IESPNClient espnClient) {
        super(transactionRepo);
        this.transactionRepo = transactionRepo;
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.espnClient = espnClient;
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
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepo.upsert(transaction);
    }

    @Override
    public List<Transaction> getDailyTransactions() {
        DateTime now = DateTime.now();
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        String today = "" + year + monthStr + dayStr;
        List<Transaction> allTransactions = Lists.newArrayList();
        List<ESPNTransaction> espnTransactions = Lists.newArrayList();
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.ADD, today, today));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.DROP, today, today));
        espnTransactions.addAll(espnClient.getTransactions(ESPNTransaction.Type.MOVE, today, today));
        Pair<List<Transaction>, List<ESPNTransaction>> pair = translateESPNTransactions(espnTransactions);
        allTransactions.addAll(pair.fst);

        handleErrorTransactions(pair.snd);

        return $.of(allTransactions).sorted().toList();
    }

    @Override
    public List<Transaction> processDailyTransactions() {
        List<Transaction> transactions = getDailyTransactions();
        List<Transaction> createdTransactions = Lists.newArrayList();
        for (Transaction t : transactions) {
            try {
                if (doesTransactionExist(t)) {
                    logger.info("Already encountered transaction: " + t);
                    continue;
                }
                PlayerSeason playerSeason = null;
                if (t.getTransactionType() == TransactionType.ADD) {
                    playerSeason = playerSeasonService.switchTeam(t.getPlayerId(), LeagueUtil.SEASON, null, t.getTeamId());
                } else if (t.getTransactionType() == TransactionType.DROP) {
                    playerSeason = playerSeasonService.switchTeam(t.getPlayerId(), LeagueUtil.SEASON, t.getTeamId(), null);
                } else if (t.getTransactionType() == TransactionType.MOVE) {
                    playerSeason = playerSeasonService.switchFantasyPosition(t.getPlayerId(), LeagueUtil.SEASON, t.getOldPosition(), t.getNewPosition());
                }
                checkNotNull(playerSeason, "PlayerSeason was null after applying transaction");
                playerSeasonService.upsert(playerSeason);
                Transaction createdTransaction = createTransaction(t);
                checkNotNull(createdTransaction, "Attempt to save transaction returned null for transaction");
                createdTransactions.add(createdTransaction);
            } catch (Exception e) {
                logger.error("Encountered an error processing transaction: " + t, e);
            }
        }
        return createdTransactions;
    }

    private Pair<List<Transaction>, List<ESPNTransaction>> translateESPNTransactions(List<ESPNTransaction> espnTransactions) {
        if (espnTransactions.size() == 0) {
            return Pair.of(Lists.newArrayList(), Lists.newArrayList());
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
        return Pair.of(transactions, errorTransactions);
    }

    private void handleErrorTransactions(List<ESPNTransaction> transactions) {
        for (ESPNTransaction transaction : transactions) {
            logger.error("Encountered a transaction that had an error: " + transaction.getText());
        }
    }

    @Nullable
    private static Position translateESPNPosition(String positionText) {
        if (positionText == null) {
            return null;
        }
        switch (positionText) {
            case "C":
                return Position.CATCHER;
            case "1B":
                return Position.FIRSTBASE;
            case "2B":
                return Position.SECONDBASE;
            case "3B":
                return Position.THIRDBASE;
            case "SS":
                return Position.SHORTSTOP;
            case "2B/SS":
                return Position.MIDDLEINFIELD;
            case "1B/3B":
                return Position.CORNERINFIELD;
            case "OF":
                return Position.OUTFIELD;
            case "UTIL":
                return Position.UTILITY;
            case "P":
                return Position.PITCHER;
            case "DL":
                return Position.DISABLEDLIST;
            default:
                return null;
        }
    }

    @Nullable
    private static TransactionType translateESPNTransactionType(ESPNTransaction.Type type) {
        switch (type) {
            case ADD:
                return TransactionType.ADD;
            case DROP:
                return TransactionType.DROP;
            case MOVE:
                return TransactionType.MOVE;
            case TRADE:
                return TransactionType.TRADE;
            default:
                return TransactionType.UNKNOWN;
        }

    }
}
