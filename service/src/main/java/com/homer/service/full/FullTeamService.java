package com.homer.service.full;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.service.DraftDollarService;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.ITeamService;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.view.TeamView;
import com.homer.util.core.$;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 4/29/16.
 */
public class FullTeamService implements IFullTeamService {

    private IPlayerSeasonService playerSeasonService;
    private ITeamService teamService;
    private IUserService userService;
    private IEmailService emailService;

    public FullTeamService(IPlayerSeasonService playerSeasonService, ITeamService teamService,
                           IUserService userService, IEmailService emailService) {
        this.playerSeasonService = playerSeasonService;
        this.teamService = teamService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public List<TeamView> getTeamSalaries() {
        List<Team> teams = teamService.getTeams();
        Map<Long, Team> teamMap = $.of(teams).toIdMap();

        ListMultimap<Long, PlayerSeason> teamToPlayers = getTeamToPlayerMap();

        List<TeamView> teamViews = Lists.newArrayList();
        for(Long teamId : teamToPlayers.keySet()) {
            TeamView tv = TeamView.from(teamMap.get(teamId));
            List<PlayerSeason> players = teamToPlayers.get(teamId);
            tv.setSalary(calculateActiveSalary(players));
            teamViews.add(tv);
        }
        return teamViews;
    }

    @Override
    public void checkSeasonSalaries() {
        List<Long> teamsOverCap = Lists.newArrayList();
        Map<Long, Integer> salaryMap = Maps.newHashMap();
        ListMultimap<Long, PlayerSeason> teamToPlayers = getTeamToPlayerMap();
        for(Long teamId : teamToPlayers.keySet()) {
            List<PlayerSeason> players = teamToPlayers.get(teamId);
            int salary = calculateActiveSalary(players);
            salaryMap.put(teamId, salary);
            if (salary > DraftDollarService.MLB_DRAFT_DOLLAR_MAX) {
                teamsOverCap.add(teamId);
            }
        }
        if (teamsOverCap.size() > 0) {
            List<String> emails = $.of(userService.getUsersForTeams(teamsOverCap)).toList(User::getEmail);
            emails.add(IEmailService.COMMISSIONER_EMAIL);

            HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV);
            htmlObject.child(HtmlObject.of(HtmlTag.P).body("The following teams have an in-season salary for their active roster higher than the allowed limit (" + DraftDollarService.MLB_DRAFT_DOLLAR_MAX + ") :"));
            HtmlObject teamList = HtmlObject.of(HtmlTag.UL);
            for(Long teamId : teamsOverCap) {
                Team team = teamService.getById(teamId);
                String teamString = String.format("%s: %d", team.getName(), salaryMap.get(teamId));
                teamList.child(HtmlObject.of(HtmlTag.LI).child(HtmlObject.of(HtmlTag.SPAN).body(teamString)));
            }
            htmlObject.child(teamList);

            EmailRequest emailRequest = new EmailRequest(emails, "Salary Cap Infraction", htmlObject);
            emailService.sendEmail(emailRequest);
        }
    }

    private ListMultimap<Long, PlayerSeason> getTeamToPlayerMap()
    {
        List<PlayerSeason> allPlayers = $.of(playerSeasonService.getActivePlayers())
                .filterToList(ps -> ps.getTeamId() != null);
        return Multimaps.index(allPlayers, PlayerSeason::getTeamId);
    }

    protected static int calculateActiveSalary(List<PlayerSeason> playerSeasons) {
        return $.of(playerSeasons)
                .filter(ps -> ps.getFantasyPosition() != Position.DISABLEDLIST && ps.getFantasyPosition() != Position.MINORLEAGUES)
                .reduceToInt(ps -> ps.getSalary());
    }
}
