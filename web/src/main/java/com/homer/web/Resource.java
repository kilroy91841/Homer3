package com.homer.web;

import com.google.common.collect.Lists;
import com.homer.service.PlayerService;
import com.homer.service.TeamService;
import com.homer.type.Player;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 2/14/16.
 */
@Path("/")
public class Resource {

    private PlayerService playerService;
    private TeamService teamService;

    public Resource() {
        playerService = new PlayerService();
        teamService = new TeamService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String helloWord() {
        return "Hello World";
    }

    @Path("player")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @Path("player/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getPlayers(@PathParam(value = "id") long id) {
        return playerService.getPlayersById(Lists.newArrayList(id));
    }

    @Path("player/search")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> searchPlayersByName(@QueryParam(value = "name") String name) {
        return playerService.searchPlayersByName(name);
    }

    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TeamView getTeam(@PathParam(value = "id") long id,
                            @QueryParam(value = "season") Integer season) {
        TeamView teamView = new TeamView();
        List<PlayerView> allPlayers = playerService.getPlayersByTeam(id);
        teamView.setTeam(teamService.getTeamById(id));
        teamView.setMajorLeaguers(allPlayers.stream().filter(p -> !p.getCurrentSeason().isMinorLeaguer()).collect(Collectors.toList()));
        teamView.setMinorLeaguers(allPlayers.stream().filter(p -> p.getCurrentSeason().isMinorLeaguer()).collect(Collectors.toList()));
        teamView.setSalary(
                teamView.getMajorLeaguers()
                        .stream()
                        .map(pv -> pv.getCurrentSeason().getSalary())
                        .reduce(Integer::sum)
                        .get()
        );
        return teamView;
    }

    @Path("/team")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<Team> getTeams() {
        return teamService.getTeams();
    }
}
