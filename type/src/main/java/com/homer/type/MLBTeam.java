package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/6/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MLBTeam implements IIntEnum<MLBTeam> {
    NEWYORKYANKEES(1, "New York Yankees", "NYY"),
    BOSTONREDSOX(2, "Boston Red Sox", "BOS"),
    BALTIMOREORIOLES(3, "Baltimore Orioles", "BAL"),
    TORONTOBLUEJAYS(4, "Toronto Blue Jays", "TOR"),
    TAMPABAYRAYS(5, "Tampa Bay Rays", "TB"),
    KANSASCITYROYALS(6, "Kansas City Royals", "KC"),
    DETROITTIGERS(7, "Detroit Tigers", "DET"),
    CHICAGOWHITESOX(8, "Chicago White Sox", "CHW"),
    CLEVELANDINDIANS(9, "Cleveland Indians", "CLE"),
    MINNESOTATWINS(10, "Minnesota Twins", "MIN"),
    TEXASRANGERS(11, "Texas Rangers", "TEX"),
    OAKLANDATHLETICS(12, "Oakland Athletics", "OAK"),
    SEATTLEMARINERS(13, "Seattle Marines", "SEA"),
    LOSANGELESANGELS(14, "Los Angeles Angels", "LAA"),
    HOUSTONASTROS(15, "Houston Astros", "HOU"),
    NEWYORKMETS(16, "New York Mets", "NYM"),
    WASHINGTONNATIONALS(17, "Washington Nationals", "WAS"),
    ATLANTABRAVES(18, "Atlanta Braves", "ATL"),
    MIAMIMARLINS(19, "Miami Marlins", "MIA"),
    PHILADELPHIAPHILLIES(20, "Philadelphia Phillies", "PHI"),
    STLOUISCARDINALS(21, "St. Louis Cardinals", "STL"),
    CHICAGOCUBS(22, "Chicago Cubs", "CHC"),
    PITTSBURGHPIRATES(23, "Pittsburgh Pirates", "PIT"),
    MILWAUKEEBREWERS(24, "Milwaukee Brewers", "MIL"),
    CINCINNATIREDS(25, "Cincinnati Reds", "CIN"),
    LOSANGELESDODGERS(26, "Los Angeles Dodgers", "LAD"),
    COLORADOROCKIES(27, "Colorado Rockies", "COL"),
    SANFRANCISCOGIANTS(28, "San Francisco Giants", "SF"),
    SANDIEGOPADRES(29, "San Diego Padres", "SD"),
    ARIZONADIAMONDBACKS(30, "Arizona Diamondbacks", "ARI");

    private final int id;
    private final String name;
    private final String abbreviation;

    private MLBTeam(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
