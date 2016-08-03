package com.homer.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.service.gather.IGatherer;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.util.core.$;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/14/16.
 */
public class Validator {

    private ITeamService teamService;
    private IGatherer gatherer;
    private IEmailService emailService;

    public Validator(ITeamService teamService, IGatherer gatherer, IEmailService emailService) {
        this.teamService = teamService;
        this.gatherer = gatherer;
        this.emailService = emailService;
    }

    public List<String> validateTeams() {
        List<Team> teams = teamService.getTeams();
        List<String> errors = Lists.newArrayList();
        for (Team team : teams) {
            List<PlayerView> players = gatherer.gatherPlayersByTeamId(team.getId(), false);
            TeamView tv = TeamView.from(team);
            tv.setMajorLeaguers($.of(players).filterToList(p -> p.getCurrentSeason().getFantasyPosition() != Position.MINORLEAGUES));

            validateCount(errors, tv.getCatcher(), 2, team.getName(), "Catcher");
            validateCount(errors, tv.getFirstBase(), 1, team.getName(), "First Base");
            validateCount(errors, tv.getSecondBase(), 1, team.getName(), "Second Base");
            validateCount(errors, tv.getShortstop(), 1, team.getName(), "Shortstop");
            validateCount(errors, tv.getThirdBase(), 1, team.getName(), "Third Base");
            validateCount(errors, tv.getMiddleInfield(), 1, team.getName(), "Middle Infield");
            validateCount(errors, tv.getCornerInfield(), 1, team.getName(), "Corner Infield");
            validateCount(errors, tv.getOutfield(), 5, team.getName(), "Outfield");
            validateCount(errors, tv.getUtility(), 1, team.getName(), "Utility");
            validateCount(errors, tv.getPitcher(), 9, team.getName(), "Pitcher");
            validateCount(errors, tv.getBench(), 0, team.getName(), "Bench");
        }

        HtmlObject htmlObj = HtmlObject.of(HtmlTag.DIV);
        for (String error : errors) {
            htmlObj.child(HtmlObject.of(HtmlTag.P).body(error));
        }

        EmailRequest emailRequest = new EmailRequest(Lists.newArrayList(IEmailService.COMMISSIONER_EMAIL),
                "Daily Lineup Error Report: " + DateTime.now().toLocalDate().toString(), htmlObj);

        emailService.sendEmail(emailRequest);

        return errors;
    }

    @Nullable
    private static void validateCount(List<String> errors, List<PlayerView> players, int expectedCount, String team, String position) {
        if (players.size() != expectedCount) {
            errors.add("Team: " + team + ", Position: " + position + ", Expected: " + expectedCount + ", Actual: " + players.size());
        }
    }
}
