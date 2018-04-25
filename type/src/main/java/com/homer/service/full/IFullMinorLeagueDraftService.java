package com.homer.service.full;

import com.homer.exception.IllegalMinorLeagueDraftPickException;
import com.homer.type.view.MinorLeagueDraftAdminView;
import com.homer.type.view.MinorLeagueDraftHistoryView;
import com.homer.type.view.MinorLeagueDraftView;
import com.homer.type.view.MinorLeaguePickView;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 6/10/16.
 */
public interface IFullMinorLeagueDraftService {

    MinorLeagueDraftView getMinorLeagueDraft(@Nullable Integer season);

    MinorLeagueDraftView adminDraft(MinorLeagueDraftAdminView view) throws Exception;

    MinorLeaguePickView draftPlayer(MinorLeaguePickView minorLeaguePick) throws IllegalMinorLeagueDraftPickException;

    MinorLeaguePickView skipPick(long pickId);

    MinorLeagueDraftHistoryView GetMinorLeagueDraftHistory();

}
