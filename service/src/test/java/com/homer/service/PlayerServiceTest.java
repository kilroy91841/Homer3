package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 3/15/16.
 */
public class PlayerServiceTest {

    private PlayerService service;
    private IPlayerRepository playerRepository;
    private static final long PLAYER_ID = 100L;

    @Before
    public void setup() {
        playerRepository = mock(IPlayerRepository.class);

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", Lists.newArrayList("Mike Trout"));
        when(playerRepository.getMany(map)).thenReturn(Lists.newArrayList(new Player()));

        Player player = new Player();
        player.setId(PLAYER_ID);
        when(playerRepository.getByIds(Lists.newArrayList(PLAYER_ID))).thenReturn(Lists.newArrayList(player));

        when(playerRepository.upsert(any(Player.class))).thenAnswer(x -> x.getArguments()[0]);
        service = new PlayerService(playerRepository);
    }

    @Test
    public void testCreate()
    {
        Player player = new Player();
        player.setFirstName("Ari");
        player.setLastName("Golub");
        player.setMlbTeamId(MLBTeam.NEWYORKYANKEES.getId());
        player.setPosition(Position.SHORTSTOP);
        service.createPlayer(player);
    }

    @Test
    public void testUpdate()
    {
        Player player = new Player();
        player.setId(PLAYER_ID);
        player.setFirstName("Ari");
        player.setLastName("Golub");
        player.setPosition(Position.SHORTSTOP);
        player.setMlbPlayerId(12345L);

        Player updatedPlayer = service.updatePlayer(player);
        assertEquals(updatedPlayer.getName(), player.getFirstName() + " " + player.getLastName());
        player.setName(updatedPlayer.getName());

        assertEquals(player, updatedPlayer);

        verify(playerRepository, times(1)).upsert(any(Player.class));
    }

    @Test
    public void testUpdateNoChanges()
    {
        Player player = new Player();
        player.setId(PLAYER_ID);

        Player updatedPlayer = service.updatePlayer(player);
        assertEquals(player, updatedPlayer);

        verify(playerRepository, never()).upsert(any(Player.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMlbTeam()
    {
        Player player = new Player();
        player.setFirstName("Ari");
        player.setLastName("Golub");
        player.setPosition(Position.SHORTSTOP);
        service.createPlayer(player);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoPosition()
    {
        Player player = new Player();
        player.setFirstName("Ari");
        player.setLastName("Golub");
        player.setMlbTeamId(MLBTeam.NEWYORKYANKEES.getId());
        service.createPlayer(player);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoFirstName()
    {
        Player player = new Player();
        player.setLastName("Golub");
        player.setMlbTeamId(MLBTeam.NEWYORKYANKEES.getId());
        player.setPosition(Position.SHORTSTOP);
        service.createPlayer(player);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoLastName()
    {
        Player player = new Player();
        player.setFirstName("Ari");
        player.setMlbTeamId(MLBTeam.NEWYORKYANKEES.getId());
        player.setPosition(Position.SHORTSTOP);
        service.createPlayer(player);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerAlreadyExists()
    {
        Player player = new Player();
        player.setFirstName("Mike");
        player.setLastName("Trout");
        player.setPosition(Position.SHORTSTOP);
        service.createPlayer(player);
    }
}
