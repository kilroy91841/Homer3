package com.homer.external.common.mlb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by MLB on 1/25/15.
 */
public class Game {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a");

    private Long gameId;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private LocalDate gameDate;
    private Integer awayScore;
    private Integer homeScore;
    private Long awayProbablePitcherId;
    private Long homeProbablePitcherId;
    private LocalDateTime gameTime;
    private String status;
    private String inning;
    private String inningState;
    private String gamedayUrl;
    private String amPm;

    public Game() { }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Long getAwayProbablePitcherId() {
        return awayProbablePitcherId;
    }

    public void setAwayProbablePitcherId(Long awayProbablePitcherId) {
        this.awayProbablePitcherId = awayProbablePitcherId;
    }

    public Long getHomeProbablePitcherId() {
        return homeProbablePitcherId;
    }

    public void setHomeProbablePitcherId(Long homeProbablePitcherId) {
        this.homeProbablePitcherId = homeProbablePitcherId;
    }

    public LocalDateTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(LocalDateTime gameTime) {
        this.gameTime = gameTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInning() {
        return inning;
    }

    public void setInning(String inning) {
        this.inning = inning;
    }

    public String getInningState() {
        return inningState;
    }

    public void setInningState(String inningState) {
        this.inningState = inningState;
    }

    public String getGamedayUrl() {
        return gamedayUrl;
    }

    public void setGamedayUrl(String gamedayUrl) {
        this.gamedayUrl = gamedayUrl;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }
}
