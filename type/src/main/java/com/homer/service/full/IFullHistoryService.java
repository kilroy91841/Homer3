package com.homer.service.full;

import com.homer.type.view.DraftDollarView;

import java.util.List;

/**
 * Created by arigolub on 8/5/16.
 */
public interface IFullHistoryService {

    DraftDollarView getDraftDollarHistory(long draftDollarId);
}
