package com.homer.service.schedule;

import com.homer.util.core.IId;
import com.homer.util.core.ISchedulable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by arigolub on 6/18/16.
 */
public interface IScheduler {

    Set<Map.Entry<SchedulingKey, SchedulingValue>> getAll();
    List<SchedulingValue> getAll(Class<?> clazz);
    @Nullable
    SchedulingValue get(SchedulingKey key);
    @Nullable
    default SchedulingValue get(Class<?> clazz, long id) {
        return get(new SchedulingKey(clazz, id));
    }

    boolean cancel(SchedulingKey key);
    default boolean cancel(Class<?> clazz, long id) {
        return cancel(new SchedulingKey(clazz, id));
    }

    boolean cancelAll(Class<?> clazz);

    <T extends IId & ISchedulable> void schedule(T obj, Runnable runnable);

}
