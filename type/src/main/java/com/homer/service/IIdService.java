package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.util.core.$;
import com.homer.util.core.IId;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/18/16.
 */
public interface IIdService<T extends IId> {

    List<T> getByIds(Collection<Long> ids);
    @Nullable
    default T getById(long id) {
        return $.of(this.getByIds(Lists.newArrayList(id))).first();
    }

    T upsert(T obj);

    List<T> getMany(Map<String, Object> filters);
}
