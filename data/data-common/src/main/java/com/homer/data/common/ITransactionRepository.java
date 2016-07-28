package com.homer.data.common;

import com.homer.type.Transaction;
import com.homer.util.core.data.IRepository;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/25/16.
 */
public interface ITransactionRepository extends IRepository<Transaction> {
    List<Transaction> getByText(String text);
}
