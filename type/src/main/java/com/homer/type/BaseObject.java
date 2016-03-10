package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.util.core.IBaseObject;
import com.homer.util.core.IDated;
import com.homer.util.core.IId;
import org.joda.time.DateTime;

import javax.persistence.Column;


/**
 * Created by arigolub on 2/14/16.
 */
public class BaseObject implements IBaseObject {

    @Column(name = "id")
    private long id;
    @Column(name = "createdDateUTC")
    @JsonIgnore
    private DateTime createdDateUTC;
    @Column(name = "updatedDateUTC")
    @JsonIgnore
    private DateTime updatedDateUTC;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getCreatedDateUTC() {
        return createdDateUTC;
    }

    public void setCreatedDateUTC(DateTime createdDateUTC) {
        this.createdDateUTC = createdDateUTC;
    }

    public DateTime getUpdatedDateUTC() {
        return updatedDateUTC;
    }

    public void setUpdatedDateUTC(DateTime updatedDateUTC) {
        this.updatedDateUTC = updatedDateUTC;
    }
}
