package com.homer.util.core.data;

import com.google.common.collect.Maps;
import com.homer.util.core.$;
import com.homer.util.core.IBaseObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Created by arigolub on 3/14/16.
 */
public interface IRepository<T extends IBaseObject> {

    List<T> getMany(Map<String, ?> filters);
    default List<T> getMany(String column, Object filter) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put(column, filter);
        return this.getMany(filters);
    }
    @Nullable
    default T get(String column, Object filter) {
        return $.of(this.getMany(column, filter)).first();
    }
    @Nullable
    default T get(Map<String, ?> filters) {
        return $.of(this.getMany(filters)).first();
    }
    default List<T> getByIds(Collection<Long> ids) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("id", ids);
        return getMany(filters);
    }
    @Nullable
    default T getById(long id) {
        List<Long> ids = $.of(id).toList();
        return $.of(this.getByIds(ids)).first();
    }
    default List<T> getAll() {
        return getMany(Maps.newHashMap());
    }

    T upsertNoHistory(T obj);

    boolean deleteNoHistory(long id);
    default boolean deleteNoHistory(T obj) {
        return this.deleteNoHistory(obj.getId());
    }
}
