package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.ITransactionRepository;
import com.homer.type.Transaction;
import com.homer.util.data.BaseRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 7/25/16.
 */
public class TransactionRepository extends BaseRepository<Transaction> implements ITransactionRepository {

    public TransactionRepository() { super(Transaction.class); }

    @Override
    public List<Transaction> getByText(String text) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("text", text);
        return getMany(filters);
    }
}
