package com.homer.external.rest.mlb;

import com.homer.external.common.mlb.MLBPlayer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by arigolub on 4/18/16.
 */
public class MLBRestClientTest {

    @Test
    public void doPlayerTest() {
        MLBRestClient client = new MLBRestClient();
        MLBPlayer player = client.getPlayer(new Long(545361));
        assertNotNull(player);
        assertEquals("Mike Trout", player.getName());
    }
}
