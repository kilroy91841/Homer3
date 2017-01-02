package com.homer.web;

import com.homer.data.common.IKeeperRepository;
import com.homer.service.IKeeperService;
import com.homer.type.Keeper;
import com.homer.web.model.ApiResponse;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static com.homer.web.RestUtility.safelyDo;

/**
 * Created by arigolub on 8/15/16.
 */
@Singleton
@Path("/keepers")
public class KeeperResource {

    private IKeeperService keeperService;

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    public KeeperResource() {
        keeperService = serviceFactory.get(IKeeperService.class);
    }

    @GET
    @Path("/{teamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getKeepers(@PathParam(value = "teamId") long teamId) {
        return safelyDo(() -> keeperService.getKeepers(teamId));
    }

    @POST
    @Path("/{teamId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse saveKeepers(@PathParam(value = "teamId") long teamId,
                                   List<Keeper> keepers) {
        return safelyDo(() -> keeperService.replaceKeepers(keepers, teamId));
    }

    @GET
    @Path("/finalize/{teamId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse saveKeepers(@PathParam(value = "teamId") long teamId) {
        return safelyDo(() -> keeperService.finalizeKeepers(teamId));
    }
}
