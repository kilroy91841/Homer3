package com.homer.service.schedule;

import com.google.common.base.Objects;
import org.joda.time.DateTime;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by arigolub on 6/18/16.
 */
public class SchedulingValue {

    private ScheduledFuture future;
    private DateTime deadlineUTC;

    public SchedulingValue(ScheduledFuture future, DateTime deadlineUTC) {
        this.future = future;
        this.deadlineUTC = deadlineUTC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingValue that = (SchedulingValue) o;
        return Objects.equal(future, that.future) &&
                Objects.equal(deadlineUTC, that.deadlineUTC);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(future, deadlineUTC);
    }

    @Override
    public String toString() {
        return "SchedulingValue{" +
                "future=" + future +
                ", deadlineUTC=" + deadlineUTC +
                '}';
    }

    public ScheduledFuture getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture future) {
        this.future = future;
    }
}
