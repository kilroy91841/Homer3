package com.homer.external.rest.mlb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arigolub on 4/18/16.
 */
public class JsonPlayer {

    @JsonProperty(value = "birth_country")
    private String birthCountry;
    @JsonProperty(value = "name_prefix")
    private String namePrefix;
    @JsonProperty(value = "name_display_first_last")
    private String nameDisplayFirstLast;
    @JsonProperty(value = "college")
    private String college;
    @JsonProperty(value = "height_inches")
    private String heightInches;
    @JsonProperty(value = "death_country")
    private String deathCountry;
    @JsonProperty(value = "age")
    private String age;
    @JsonProperty(value = "name_display_first_last_html")
    private String nameDisplayFirstLastHTML;
    @JsonProperty(value = "gender")
    private String gender;
    @JsonProperty(value = "height_feet")
    private String heightFeet;
    @JsonProperty(value = "pro_debut_date")
    private String proDebutDate;
    @JsonProperty(value = "death_date")
    private String deathDate;
    @JsonProperty(value = "primary_position")
    private String primaryPosition;
    @JsonProperty(value = "birth_date")
    private String birthDate;
    @JsonProperty(value = "team_abbrev")
    private String teamAbbrev;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "name_display_last_first_html")
    private String nameDisplayLastFirstHTML;
    @JsonProperty(value = "throws")
    private String throwss;
    @JsonProperty(value = "death_city")
    private String deathCity;
    @JsonProperty(value = "primary_position_txt")
    private String primaryPositionTxt;
    @JsonProperty(value = "high_school")
    private String highSchool;
    @JsonProperty(value = "name_display_roster_html")
    private String nameDisplayRosterHTML;
    @JsonProperty(value = "name_use")
    private String nameUse;
    @JsonProperty(value = "player_id")
    private String playerID;
    @JsonProperty(value = "status_date")
    private String statusDate;
    @JsonProperty(value = "primary_stat_type")
    private String primaryStatType;
    @JsonProperty(value = "team_id")
    private String teamID;
    @JsonProperty(value = "active_sw")
    private String activeSw;
    @JsonProperty(value = "primary_sport_code")
    private String primarySportCode;
    @JsonProperty(value = "birth_state")
    private String birthState;
    @JsonProperty(value = "weight")
    private String weight;
    @JsonProperty(value = "name_middle")
    private String nameMiddle;
    @JsonProperty(value = "name_display_roster")
    private String nameDisplayRoster;
    @JsonProperty(value = "end_date")
    private String endDate;
    @JsonProperty(value = "jersey_number")
    private String jerseyNumber;
    @JsonProperty(value = "death_state")
    private String deathState;
    @JsonProperty(value = "name_first")
    private String nameFirst;
    @JsonProperty(value = "bats")
    private String bats;
    @JsonProperty(value = "team_code")
    private String teamCode;
    @JsonProperty(value = "birth_city")
    private String birthCity;
    @JsonProperty(value = "name_nick")
    private String nameNick;
    @JsonProperty(value = "status_code")
    private String statusCode;
    @JsonProperty(value = "name_matrilineal")
    private String nameMatrilineal;
    @JsonProperty(value = "team_name")
    private String teamName;
    @JsonProperty(value = "name_display_last_first")
    private String nameDisplayLastFirst;
    @JsonProperty(value = "twitter_id")
    private String twitterID;
    @JsonProperty(value = "name_title")
    private String nameTitle;
    @JsonProperty(value = "file_code")
    private String fileCode;
    @JsonProperty(value = "name_last")
    private String nameLast;
    @JsonProperty(value = "start_date")
    private String startDate;
    @JsonProperty(value = "name_full")
    private String nameFull;

    //region getters + setters

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getNameDisplayFirstLast() {
        return nameDisplayFirstLast;
    }

    public void setNameDisplayFirstLast(String nameDisplayFirstLast) {
        this.nameDisplayFirstLast = nameDisplayFirstLast;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(String heightInches) {
        this.heightInches = heightInches;
    }

    public String getDeathCountry() {
        return deathCountry;
    }

    public void setDeathCountry(String deathCountry) {
        this.deathCountry = deathCountry;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNameDisplayFirstLastHTML() {
        return nameDisplayFirstLastHTML;
    }

    public void setNameDisplayFirstLastHTML(String nameDisplayFirstLastHTML) {
        this.nameDisplayFirstLastHTML = nameDisplayFirstLastHTML;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(String heightFeet) {
        this.heightFeet = heightFeet;
    }

    public String getProDebutDate() {
        return proDebutDate;
    }

    public void setProDebutDate(String proDebutDate) {
        this.proDebutDate = proDebutDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getPrimaryPosition() {
        return primaryPosition;
    }

    public void setPrimaryPosition(String primaryPosition) {
        this.primaryPosition = primaryPosition;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTeamAbbrev() {
        return teamAbbrev;
    }

    public void setTeamAbbrev(String teamAbbrev) {
        this.teamAbbrev = teamAbbrev;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNameDisplayLastFirstHTML() {
        return nameDisplayLastFirstHTML;
    }

    public void setNameDisplayLastFirstHTML(String nameDisplayLastFirstHTML) {
        this.nameDisplayLastFirstHTML = nameDisplayLastFirstHTML;
    }

    public String getDeathCity() {
        return deathCity;
    }

    public void setDeathCity(String deathCity) {
        this.deathCity = deathCity;
    }

    public String getPrimaryPositionTxt() {
        return primaryPositionTxt;
    }

    public void setPrimaryPositionTxt(String primaryPositionTxt) {
        this.primaryPositionTxt = primaryPositionTxt;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public String getNameDisplayRosterHTML() {
        return nameDisplayRosterHTML;
    }

    public void setNameDisplayRosterHTML(String nameDisplayRosterHTML) {
        this.nameDisplayRosterHTML = nameDisplayRosterHTML;
    }

    public String getNameUse() {
        return nameUse;
    }

    public void setNameUse(String nameUse) {
        this.nameUse = nameUse;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getPrimaryStatType() {
        return primaryStatType;
    }

    public void setPrimaryStatType(String primaryStatType) {
        this.primaryStatType = primaryStatType;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getActiveSw() {
        return activeSw;
    }

    public void setActiveSw(String activeSw) {
        this.activeSw = activeSw;
    }

    public String getPrimarySportCode() {
        return primarySportCode;
    }

    public void setPrimarySportCode(String primarySportCode) {
        this.primarySportCode = primarySportCode;
    }

    public String getBirthState() {
        return birthState;
    }

    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNameMiddle() {
        return nameMiddle;
    }

    public void setNameMiddle(String nameMiddle) {
        this.nameMiddle = nameMiddle;
    }

    public String getNameDisplayRoster() {
        return nameDisplayRoster;
    }

    public void setNameDisplayRoster(String nameDisplayRoster) {
        this.nameDisplayRoster = nameDisplayRoster;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getDeathState() {
        return deathState;
    }

    public void setDeathState(String deathState) {
        this.deathState = deathState;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getBats() {
        return bats;
    }

    public void setBats(String bats) {
        this.bats = bats;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getNameNick() {
        return nameNick;
    }

    public void setNameNick(String nameNick) {
        this.nameNick = nameNick;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getNameMatrilineal() {
        return nameMatrilineal;
    }

    public void setNameMatrilineal(String nameMatrilineal) {
        this.nameMatrilineal = nameMatrilineal;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getNameDisplayLastFirst() {
        return nameDisplayLastFirst;
    }

    public void setNameDisplayLastFirst(String nameDisplayLastFirst) {
        this.nameDisplayLastFirst = nameDisplayLastFirst;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(String twitterID) {
        this.twitterID = twitterID;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public String getThrowss() {
        return throwss;
    }

    public void setThrowss(String throwss) {
        this.throwss = throwss;
    }

    //endregion
}
