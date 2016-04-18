package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 3/15/16.
 */
public class PlayerServiceTest {

    private PlayerService service;

    @Before
    public void setup() {
        IPlayerRepository playerRepository = mock(IPlayerRepository.class);
        when(playerRepository.getMany(anyMap())).thenAnswer(x -> {
            List<Player> players = Lists.newArrayList();
            Map<String, Object> map = (Map)x.getArguments()[0];
            String playerName = (String)((List)map.get("name")).get(0);
            if ("Mike Trout".equals(playerName)) {
                players.add(new Player());
            }
            return players;
        });
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
