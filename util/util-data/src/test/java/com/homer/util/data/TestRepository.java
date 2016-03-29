package com.homer.util.data;

/**
 * Created by arigolub on 3/14/16.
 */
public class TestRepository extends BaseVersionedRepository<ChildObject, HistoryChildObject> {

    public TestRepository() {
        super(ChildObject.class, HistoryChildObject.class);
    }
}
