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

/**
 * Created by arigolub on 7/31/16.
 */
@Singleton
@Path("/standings")
public class StandingsResource {

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private IStandingService standingService;
    private ITeamDailyService teamDailyService;
    private IPlayerDailyService playerDailyService;

    public StandingsResource() {
        this.playerDailyService = serviceFactory.get(IPlayerDailyService.class);
        this.standingService = serviceFactory.get(IStandingService.class);
        this.teamDailyService = serviceFactory.get(ITeamDailyService.class);
    }

    @GET
    @Path("/compute")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse computeStandingsForDate(@QueryParam(value = "date") String dateString,
                                 @QueryParam(value = "refreshTeamDailies") boolean refreshTeamDailies) {
        try {
            DateTime date = DateTime.parse(dateString).withMillisOfDay(0);
            return new ApiResponse("success", standingService.computeStandingsForDate(date, refreshTeamDailies));
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getLatestStandings(@QueryParam(value = "date") String dateString) {
        try {
            List<Standing> standings;
            if (dateString == null) {
                standings = standingService.getLatestStandings();
            } else {
                standings = standingService.getByDate(DateTime.parse(dateString).withMillisOfDay(0));
            }
            return new ApiResponse("success", standings);
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }

    @GET
    @Path("/between")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse computeStandingsBetweenDates(@QueryParam(value = "start") String startString,
                                                    @QueryParam(value = "end") String endString) {
        try {
            DateTime start = DateTime.parse(startString).withMillisOfDay(0);
            DateTime end = DateTime.parse(endString).withMillisOfDay(0);
            return new ApiResponse("success", standingService.computeStandingsBetweenDates(start, end));
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }

    @GET
    @Path("/updateToLatest")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateToLatest(@QueryParam(value = "refreshTeamDailies") boolean refreshTeamDailies) {
        try {
            DateTime date = ESPNUtility.SCORING_PERIOD_1;
            List<Standing> standings = Lists.newArrayList();
            while (date.isBeforeNow()) {
                standings = standingService.computeStandingsForDate(date, refreshTeamDailies);
                date = date.plusDays(1);
            }
            return new ApiResponse("success", standings);
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }
}
