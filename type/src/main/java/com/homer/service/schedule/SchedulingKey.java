package com.homer.service.schedule;

import com.google.common.base.Objects;

/**
 * Created by arigolub on 6/18/16.
 */
public class SchedulingKey {

    private final Class<?> clazz;
    private final long id;

    public SchedulingKey(Class<?> clazz, long id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingKey that = (SchedulingKey) o;
        return id == that.id &&
                Objects.equal(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clazz, id);
    }

    @Override
    public String toString() {
        return "SchedulingKey{" +
                "clazz=" + clazz +
                ", id=" + id +
                '}';
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public long getId() {
        return id;
    }
}
