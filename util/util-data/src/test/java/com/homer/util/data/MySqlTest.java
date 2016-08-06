package com.homer.util.data;

import com.google.common.collect.Maps;
import com.homer.util.core.data.IRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by arigolub on 2/14/16.
 */
public class MySqlTest {

    private IRepository<ChildObject> repo;

    @Before
    public void setup() {
        this.repo = new TestRepository();
    }

    //TODO Add test: attempting to update a column with update = false will throw an exception

    @Test
    public void doTest() throws Exception {
        ChildObject upsertTest = new ChildObject();
        upsertTest.setCount(5);
        upsertTest.setLongObject(null);
        upsertTest.setName("Test2");
        upsertTest.setMyBool(true);
        upsertTest.setDummyEnum(DummyEnum.VALUE1);
        upsertTest.setCreatedDateUTC(DateTime.now());
        upsertTest.setUpdatedDateUTC(DateTime.now());

        ChildObject upserted = repo.upsertNoHistory(upsertTest);
        assertTrue(upserted.getId() > 0);
        upsertTest.setId(upserted.getId());
        assertEquals(upsertTest, upserted);

        List<ChildObject> fetchedList = repo.getMany(Maps.newHashMap());
        assertEquals(1, fetchedList.size());
        ChildObject fetched = fetchedList.get(0);
        assertEquals(upserted, fetched);

        upsertTest.setLongObject(36L);
        upserted = repo.upsertNoHistory(upsertTest);
        assertEquals(upsertTest, upserted);

        fetchedList = repo.getMany(Maps.newHashMap());
        assertEquals(1, fetchedList.size());

        fetched = fetchedList.get(0);
        assertEquals(upserted, fetched);

        assertTrue(repo.deleteNoHistory(fetched.getId()));

        fetchedList = repo.getMany(Maps.newHashMap());
        assertEquals(0, fetchedList.size());
    }
}
