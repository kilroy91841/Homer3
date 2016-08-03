package com.homer.external.rest.espn;

import com.homer.external.common.espn.ESPNPlayer;
import com.homer.external.common.espn.ESPNTransaction;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by arigolub on 7/25/16.
 */
public class ESPNRestClientTest {

    private ESPNRestClient client;

    @Before
    public void setup() {
        client = new ESPNRestClient();
    }

    @Test
    public void testClient() {
        List<ESPNTransaction> espnTrans = client.getTransactions(1, ESPNTransaction.Type.ADD, "20160725", "20160725");
        assertEquals(1, espnTrans.size());
        ESPNTransaction add = espnTrans.get(0);
        validateTransaction(add);
        assertEquals(add.getTransDate(), new DateTime("2015-07-25T07:35:00.000-04:00"));
        assertEquals("Jake Peavy", add.getPlayerName());
        assertEquals(1, add.getTeamId());
        assertEquals(ESPNTransaction.Type.ADD, add.getType());

        espnTrans = client.getTransactions(1, ESPNTransaction.Type.DROP, "20160725", "20160725");
        assertEquals(1, espnTrans.size());
        ESPNTransaction drop = espnTrans.get(0);
        validateTransaction(drop);
        assertEquals("Nathan Eovaldi", drop.getPlayerName());
        assertEquals(1, drop.getTeamId());
        assertEquals(ESPNTransaction.Type.DROP, drop.getType());

        espnTrans = client.getTransactions(1, ESPNTransaction.Type.MOVE, "20160725", "20160725");
        assertEquals(1, espnTrans.size());
        ESPNTransaction move = espnTrans.get(0);
        validateTransaction(move);
        assertEquals("Jake Peavy", move.getPlayerName());
        assertEquals(1, move.getTeamId());
        assertEquals(ESPNTransaction.Type.MOVE, move.getType());
        assertEquals("Bench", move.getOldPosition());
        assertEquals("P", move.getNewPosition());
    }

    @Test
    public void testMultipleMovesInOne() {
        List<ESPNTransaction> espnTrans = client.getTransactions(8, ESPNTransaction.Type.MOVE, "20160731", "20160731");
        assertEquals(6, espnTrans.size());
        for(ESPNTransaction espnTransaction : espnTrans) {
            validateTransaction(espnTransaction);
        }
    }

    @Test
    public void testMultipleDropsInOne() {
        List<ESPNTransaction> espnTrans = client.getTransactions(12, ESPNTransaction.Type.DROP, "20160731", "20160731");
        assertEquals(7, espnTrans.size());
        for(ESPNTransaction espnTransaction : espnTrans) {
            validateTransaction(espnTransaction);
        }
    }

    @Test
    public void testMoveToPositionWithSlash() {
        List<ESPNTransaction> espnTrans = client.getTransactions(6, ESPNTransaction.Type.MOVE, "20160802", "20160802");
        assertEquals(3, espnTrans.size());
        for(ESPNTransaction espnTransaction : espnTrans) {
            validateTransaction(espnTransaction);
        }
    }

    @Test
    public void testRoster() {
        List<ESPNPlayer> espnPlayers = client.getRoster(1, 119);
        assertEquals(23, espnPlayers.size());
    }

    private static void validateTransaction(ESPNTransaction transaction) {
        assertNotNull(transaction.getTransDate());
        assertNotNull(transaction.getPlayerName());
        assertNotNull(transaction.getType());
        assertTrue(transaction.getTeamId() > 0);
        assertNotNull(transaction.getText());
        if (ESPNTransaction.Type.MOVE.equals(transaction.getType())) {
            assertNotNull(transaction.getOldPosition());
            assertNotNull(transaction.getNewPosition());
        }
    }
}
