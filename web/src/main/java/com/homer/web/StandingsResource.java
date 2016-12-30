package com.homer.web;

import com.google.common.collect.Lists;
import com.homer.service.IPlayerDailyService;
import com.homer.service.IStandingService;
import com.homer.service.ITeamDailyService;
import com.homer.service.utility.ESPNUtility;
import com.homer.type.Standing;
import com.homer.web.model.ApiResponse;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.function.Supplier;

import static com.homer.web.RestUtility.safelyDo;

/**
 * Created by arigolub on 7/31/16.
 */
@Singleton
@Path("/standings")
public class StandingsResource {

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private IStandingService standingService;

    public StandingsResource() {
        this.standingService = serviceFactory.get(IStandingService.class);
    }

    @GET
    @Path("/compute")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse computeStandingsForDate(@QueryParam(value = "date") String dateString,
                                 @QueryParam(value = "refreshTeamDailies") boolean refreshTeamDailies) {
        return safelyDo(() ->
                standingService.computeStandingsForDate(DateTime.parse(dateString).withMillisOfDay(0), refreshTeamDailies));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getLatestStandings(@QueryParam(value = "date") String dateString) {
        return safelyDo(() -> {
            List<Standing> standings;
            if (dateString == null) {
                standings = standingService.getLatestStandings();
            } else {
                standings = standingService.getByDate(DateTime.parse(dateString).withMillisOfDay(0));
            }
            return standings;
        });
    }

    @GET
    @Path("/between")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse computeStandingsBetweenDates(@QueryParam(value = "start") String startString,
                                                    @QueryParam(value = "end") String endString) {
        return safelyDo(() -> {
            DateTime start = DateTime.parse(startString).withMillisOfDay(0);
            DateTime end = DateTime.parse(endString).withMillisOfDay(0);
            return standingService.computeStandingsBetweenDates(start, end);
        });
    }

    @GET
    @Path("/updateToLatest")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateToLatest(@QueryParam(value = "refreshTeamDailies") boolean refreshTeamDailies) {
        return safelyDo(() -> {
            DateTime date = ESPNUtility.SCORING_PERIOD_1;
            List<Standing> standings = Lists.newArrayList();
            while (date.isBeforeNow()) {
                standings = standingService.computeStandingsForDate(date, refreshTeamDailies);
                date = date.plusDays(1);
            }
            return standings;
        });
    }

    @GET
    @Path("/activeStats")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getActiveStats(@QueryParam(value = "teamId") long teamId) {
        return safelyDo(() ->
                standingService.getActiveStatsForTeam(teamId)
        );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/finalizeSeptember")
    public ApiResponse finalizeSeptemberStandings() {
        return RestUtility.safelyDo(() -> standingService.finalizeSeptemberStandings(2016));
    }
}
