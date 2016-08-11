package com.homer.service;

import com.homer.service.full.IFullHistoryService;
import com.homer.type.view.DraftDollarView;
import com.homer.web.ServiceFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by arigolub on 8/5/16.
 */
public class FullHistoryServiceTest {

    private static final long DRAFT_DOLLAR_ID = 2;
    private IFullHistoryService fullHistoryService;

    @Before
    public void setup() {
        fullHistoryService = ServiceFactory.getInstance().get(IFullHistoryService.class);
    }

    @Test
    public void test_draftDollarHistory() {
        DraftDollarView draftDollars = fullHistoryService.getDraftDollarHistory(DRAFT_DOLLAR_ID);
        assertEquals(4, draftDollars.getHistoryDraftDollars().size());
    }
}
