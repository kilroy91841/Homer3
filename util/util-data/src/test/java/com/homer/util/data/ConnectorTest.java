package com.homer.util.data;

import com.homer.util.core.IBaseObject;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by arigolub on 2/14/16.
 */
public class ConnectorTest {

    @Test
    public void doTest() throws SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        Map<String, String> filters = new HashMap<>();
//        filters.put("name", "Test1");
//        List<ChildObject> players = Connector.get(ChildObject.class, filters);
//        assertEquals(1, players.size());
//        ChildObject player = players.get(0);
//        ChildObject testObject = new ChildObject();
//        testObject.setCount(3);
//        testObject.setLongObject(null);
//        testObject.setName("Test1");
//        testObject.setCreatedDateUTC(new DateTime(1000 * 1455478789));
//        testObject.setUpdatedDateUTC(new DateTime(1000 * 1455478789));
//        assertEquals(testObject, player);

        ChildObject upsertTest = new ChildObject();
        upsertTest.setCount(5);
        upsertTest.setLongObject(null);
        upsertTest.setName("Test2");
        upsertTest.setMyBool(true);
        upsertTest.setCreatedDateUTC(new DateTime(1000 * 1455478700));
        upsertTest.setUpdatedDateUTC(new DateTime(1000 * 1455478700));
        ChildObject upserted = Connector.upsert(upsertTest);
        assertTrue(upserted.getId() > 0);
        upsertTest.setId(upserted.getId());
        assertEquals(upsertTest, upserted);

        upsertTest.setLongObject(36L);
        upserted = Connector.upsert(upsertTest);
        assertEquals(upsertTest, upserted);

        Map<String, String> filters = new HashMap<>();
        filters.put("count", "5");
        List<ChildObject> players = Connector.get(ChildObject.class, filters);
        //assertEquals(1, players.size());
        //assertEquals(upsertTest, players.get(0));
    }
}
