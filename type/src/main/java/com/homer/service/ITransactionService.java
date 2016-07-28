package com.homer.service;

import com.homer.type.Transaction;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/25/16.
 */
public interface ITransactionService extends IIdService<Transaction> {

    List<Transaction> getTransactions();

    boolean doesTransactionExist(Transaction transaction);

    Transaction createTransaction(Transaction transaction);

    List<Transaction> processDailyTransactions();

    List<Transaction> getDailyTransactions();
}
