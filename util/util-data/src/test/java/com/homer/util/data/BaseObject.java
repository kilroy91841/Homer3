package com.homer.util.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.util.core.IBaseObject;
import org.joda.time.DateTime;

import javax.persistence.Column;

/**
 * Created by arigolub on 2/14/16.
 */
public class BaseObject implements IBaseObject {

    @Column
    private long id;
    @Column
    @JsonIgnore
    private DateTime createdDateUTC;
    @Column
    @JsonIgnore
    private DateTime updatedDateUTC;

    @Override
    public DateTime getCreatedDateUTC() {
        return createdDateUTC;
    }

    @Override
    public void setCreatedDateUTC(DateTime createdDateUTC) {
        this.createdDateUTC = createdDateUTC;
    }

    @Override
    public DateTime getUpdatedDateUTC() {
        return updatedDateUTC;
    }

    @Override
    public void setUpdatedDateUTC(DateTime updatedDateUTC) {
        this.updatedDateUTC = updatedDateUTC;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseObject that = (BaseObject) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "BaseObject{" +
                "id=" + id +
                ", createdDateUTC=" + createdDateUTC +
                ", updatedDateUTC=" + updatedDateUTC +
                '}';
    }
}
