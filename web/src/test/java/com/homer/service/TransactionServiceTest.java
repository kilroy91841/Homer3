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

    public static void main(String[] args) {
        ITransactionService transactionService = new TransactionService(
                new TransactionRepository(),
                new PlayerService(new PlayerRepository()),
                new PlayerSeasonService(new PlayerSeasonRepository()),
                new ESPNRestClient(),
                new AWSEmailService());
        
        List<Transaction> trans = transactionService.processDailyTransactions();
        for (Transaction t : trans) {
            System.out.println(t);
        }
    }
}
