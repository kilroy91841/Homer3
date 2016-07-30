package com.homer.type;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/29/16.
 */
@Table(name = "player_dailies", schema = "homer")
public class PlayerDaily extends BaseObject {

    @Column
    private DateTime date;
    @Column(updatable = false)
    private long playerId;
    @Nullable
    @Column
    private Long teamId;
    @Nullable
    @Column
    private Position fantasyPosition;
    @Column
    private int scoringPeriodId;

    //shared stats
    @Nullable
    @Column
    private Integer walks;

    //hitting stats
    @Nullable
    @Column
    private Integer atBats;
    @Nullable
    @Column
    private Integer homeRuns;
    @Nullable
    @Column
    private Integer runs;
    @Nullable
    @Column
    private Integer rbi;
    @Nullable
    @Column
    private Integer stolenBases;
    @Nullable
    @Column
    private Integer hitByPitches;
    @Nullable
    @Column
    private Integer sacFlies;
    @Nullable
    @Column
    private Integer totalBases;

    //pitching stats
    @Nullable
    @Column
    private Integer strikeouts;
    @Nullable
    @Column
    private Integer wins;
    @Nullable
    @Column
    private Integer saves;
    @Nullable
    @Column
    private Double inningsPitched;
    @Nullable
    @Column
    private Integer hits;
    @Nullable
    @Column
    private Integer earnedRuns;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(@Nullable Long teamId) {
        this.teamId = teamId;
    }

    public int getScoringPeriodId() {
        return scoringPeriodId;
    }

    public void setScoringPeriodId(int scoringPeriodId) {
        this.scoringPeriodId = scoringPeriodId;
    }

    @Nullable
    public Position getFantasyPosition() {
        return fantasyPosition;
    }

    public void setFantasyPosition(@Nullable Position fantasyPosition) {
        this.fantasyPosition = fantasyPosition;
    }

    @Nullable
    public Integer getWalks() {
        return walks;
    }

    public void setWalks(@Nullable Integer walks) {
        this.walks = walks;
    }

    @Nullable
    public Integer getAtBats() {
        return atBats;
    }

    public void setAtBats(@Nullable Integer atBats) {
        this.atBats = atBats;
    }

    @Nullable
    public Integer getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(@Nullable Integer homeRuns) {
        this.homeRuns = homeRuns;
    }

    @Nullable
    public Integer getRuns() {
        return runs;
    }

    public void setRuns(@Nullable Integer runs) {
        this.runs = runs;
    }

    @Nullable
    public Integer getRbi() {
        return rbi;
    }

    public void setRbi(@Nullable Integer rbi) {
        this.rbi = rbi;
    }

    @Nullable
    public Integer getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(@Nullable Integer stolenBases) {
        this.stolenBases = stolenBases;
    }

    @Nullable
    public Integer getHitByPitches() {
        return hitByPitches;
    }

    public void setHitByPitches(@Nullable Integer hitByPitches) {
        this.hitByPitches = hitByPitches;
    }

    @Nullable
    public Integer getSacFlies() {
        return sacFlies;
    }

    public void setSacFlies(@Nullable Integer sacFlies) {
        this.sacFlies = sacFlies;
    }

    @Nullable
    public Integer getTotalBases() {
        return totalBases;
    }

    public void setTotalBases(@Nullable Integer totalBases) {
        this.totalBases = totalBases;
    }

    @Nullable
    public Integer getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(@Nullable Integer strikeouts) {
        this.strikeouts = strikeouts;
    }

    @Nullable
    public Integer getWins() {
        return wins;
    }

    public void setWins(@Nullable Integer wins) {
        this.wins = wins;
    }

    @Nullable
    public Integer getSaves() {
        return saves;
    }

    public void setSaves(@Nullable Integer saves) {
        this.saves = saves;
    }

    @Nullable
    public Double getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(@Nullable Double inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    @Nullable
    public Integer getHits() {
        return hits;
    }

    public void setHits(@Nullable Integer hits) {
        this.hits = hits;
    }

    @Nullable
    public Integer getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(@Nullable Integer earnedRuns) {
        this.earnedRuns = earnedRuns;
    }
}
