package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IPlayerRepository;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 3/15/16.
 */
public class PlayerServiceTest {

    private IPlayerService service;

    public void setup() {
        IPlayerRepository playerRepository = mock(IPlayerRepository.class);
        when(playerRepository.getMany(anyMap())).thenReturn(Lists.newArrayList());
        service = new PlayerService(playerRepository);
    }
}
