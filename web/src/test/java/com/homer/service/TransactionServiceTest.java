package com.homer.service;

import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.data.TransactionRepository;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.type.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by arigolub on 7/27/16.
 */
public class TransactionServiceTest {

    private ITransactionService transactionService;

    @Before
    public void setup() {
        transactionService = new TransactionService(
                new TransactionRepository(),
                new PlayerService(new PlayerRepository()),
                new PlayerSeasonService(new PlayerSeasonRepository()),
                new ESPNRestClient(),
                new AWSEmailService());
    }

    @Test
    public void testIt() {
        List<Transaction> trans = transactionService.processDailyTransactions();
        for (Transaction t : trans) {
            System.out.println(t);
        }
    }
}
