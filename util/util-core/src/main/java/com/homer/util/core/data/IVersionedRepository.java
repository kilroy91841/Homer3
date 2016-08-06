package com.homer.util.core.data;

import com.homer.util.core.IBaseObject;
import com.homer.util.core.IHistoryObject;

import java.util.List;

/**
 * Created by arigolub on 8/5/16.
 */
public interface IVersionedRepository<T extends IBaseObject, H extends IHistoryObject> extends IRepository<T> {

    T upsert(T obj);
    List<H> getHistories(long id);

    boolean delete(long id);
    default boolean delete(T obj) {
        return this.delete(obj.getId());
    }
}
