package com.homer.service.schedule;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.util.core.$;
import com.homer.util.core.IId;
import com.homer.util.core.ISchedulable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by arigolub on 6/18/16.
 */
public class Scheduler implements IScheduler {

    private final static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(3);
    private final static HashMap<SchedulingKey, SchedulingValue> MAP = Maps.newHashMap();

    @Nullable
    @Override
    public SchedulingValue get(SchedulingKey key) {
        return MAP.get(key);
    }

    @Override
    public Set<Map.Entry<SchedulingKey, SchedulingValue>> getAll() {
        return MAP.entrySet();
    }

    @Override
    public List<SchedulingValue> getAll(Class<?> clazz) {
        return $.of(MAP.entrySet()).filter(es -> clazz.equals(es.getKey().getClazz())).toList(es -> es.getValue());
    }

    @Override
    public boolean cancel(SchedulingKey key) {
        SchedulingValue future = MAP.remove(key);
        if (future != null) {
            return future.getFuture().cancel(false);
        }
        return true;
    }

    @Override
    public boolean cancelAll(Class<?> clazz) {
        boolean success = true;
        for (SchedulingKey key :MAP.keySet()) {
            if (key.getClazz().equals(clazz)) {
                success = success && cancel(key);
            }
        }
        return success;
    }

    public <T extends IId & ISchedulable> void schedule(T obj, Runnable runnable) {
        ScheduledFuture future = SCHEDULER.schedule(runnable,
                obj.getDeadlineUTC().getMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        MAP.put(new SchedulingKey(obj.getClass(), obj.getId()), new SchedulingValue(future, obj.getDeadlineUTC()));
    }
}
