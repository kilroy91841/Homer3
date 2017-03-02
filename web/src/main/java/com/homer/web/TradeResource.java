package com.homer.web;

import com.homer.service.full.IFullTradeService;
import com.homer.type.Trade;
import com.homer.web.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.homer.web.RestUtility.safelyDo;

/**
 * Created by arigolub on 8/21/16.
 */
@Singleton
@Path("/trade")
public class TradeResource {

    private IFullTradeService fullTradeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    public TradeResource() {
        fullTradeService = serviceFactory.get(IFullTradeService.class);
    }

    @AuthRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse proposeTrade(Trade trade) {
        return safelyDo(() -> fullTradeService.proposeTrade(trade), (ignored) -> "Trade proposed!");
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/team/{teamId}")
    public ApiResponse getTrades(@PathParam(value = "teamId") long teamId) {
        return safelyDo(() -> fullTradeService.getTradesForTeam(teamId));
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{tradeId}/accept")
    @POST
    public ApiResponse acceptTrade(@PathParam(value = "tradeId") long tradeId) {
        return safelyDo(() -> fullTradeService.acceptTrade(tradeId), (ignored) -> "Trade Accepted!");
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{tradeId}/reject")
    @POST
    public ApiResponse rejectTrade(@PathParam(value = "tradeId") long tradeId) {
        return safelyDo(() -> fullTradeService.rejectTrade(tradeId), (ignored) -> "Trade Rejected!");
    }

    @AuthRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/admin/force")
    public ApiResponse forceTrade(Trade trade) {
        return safelyDo(() -> {
            Trade proposeTrade = fullTradeService.proposeTrade(trade);
            return acceptTrade(proposeTrade.getId());
        }, (ignored) -> "Trade Forced!");
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{tradeId}/cancel")
    @POST
    public ApiResponse cancelTrade(@PathParam(value = "tradeId") long tradeId) {
        return safelyDo(() -> fullTradeService.cancelTrade(tradeId), (ignored) -> "Trade Cancelled!");
    }
}
