package com.homer.service;

import com.homer.util.core.IBaseObject;
import com.homer.util.core.data.IRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/18/16.
 */
public class BaseIdService<T extends IBaseObject> implements IIdService<T> {

    private IRepository<T> repo;

    public BaseIdService(IRepository<T> repo) {
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
}
