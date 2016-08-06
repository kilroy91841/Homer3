package com.homer.service;

import com.homer.util.core.IBaseObject;
import com.homer.util.core.IHistoryObject;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 8/5/16.
 */
public class BaseVersionedIdService<T extends IBaseObject, H extends IHistoryObject> implements IIdService<T> {

    private IVersionedRepository<T, H> repo;

    public BaseVersionedIdService(IVersionedRepository<T, H> repo) {
        this.repo = repo;
    }

    @Override
    public List<T> getByIds(Collection<Long> ids) {
        return repo.getByIds(ids);
    }

    @Override
    public T upsert(T obj) {
        return repo.upsert(obj);
    }

    @Override
    public List<T> getMany(Map<String, Object> filters) {
        return repo.getMany(filters);
    }
}
