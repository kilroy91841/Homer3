package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/6/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MLBTeam implements IIntEnum<MLBTeam> {
    LOSANGELESANGELS(108, "ana", "Los Angeles Angels", "LAA"),
    ARIZONADIAMONDBACKS(109, "ari", "Arizona Diamondbacks", "ARI"),
    ATLANTABRAVES(144, "atl", "Atlanta Braves", "ATL"),
    BALTIMOREORIOLES(110, "bal", "Baltimore Orioles", "BAL"),
    BOSTONREDSOX(111, "bos", "Boston Red Sox", "BOS"),
    CHICAGOCUBS(112, "chn", "Chicago Cubs", "CHC"),
    CINCINNATIREDS(113, "cin", "Cincinnati Reds", "CIN"),
    CLEVELANDINDIANS(114, "cle", "Cleveland Indians", "CLE"),
    COLORADOROCKIES(115, "col", "Colorado Rockies", "COL"),
    CHICAGOWHITESOX(145, "cha", "Chicago White Sox", "CHW"),
    DETROITTIGERS(116, "det", "Detroit Tigers", "DET"),
    HOUSTONASTROS(117, "hou", "Houston Astros", "HOU"),
    KANSASCITYROYALS(118, "kca", "Kansas City Royals", "KC"),
    LOSANGELESDODGERS(119, "lan", "Los Angeles Dodgers", "LAD"),
    MIAMIMARLINS(146, "mia", "Miami Marlins", "MIA"),
    MILWAUKEEBREWERS(158, "mil", "Milwaukee Brewers", "MIL"),
    MINNESOTATWINS(142, "min", "Minnesota Twins", "MIN"),
    NEWYORKMETS(121, "nyn", "New York Mets", "NYM"),
    NEWYORKYANKEES(147, "nya", "New York Yankees", "NYY"),
    OAKLANDATHLETICS(133, "oak", "Oakland Athletics", "OAK"),
    PHILADELPHIAPHILLIES(143, "phi", "Philadelphia Phillies", "PHI"),
    PITTSBURGHPIRATES(134, "pit", "Pittsburgh Pirates", "PIT"),
    SANDIEGOPADRES(135, "sdn", "San Diego Padres", "SD"),
    SEATTLEMARINERS(136, "sea", "Seattle Mariners", "SEA"),
    SANFRANCISCOGIANTS(137, "sfn", "San Francisco Giants", "SF"),
    STLOUISCARDINALS(138, "sln", "St. Louis Cardinals", "STL"),
    TAMPABAYRAYS(139, "tba", "Tampa Bay Rays", "TB"),
    TEXASRANGERS(140, "tex", "Texas Rangers", "TEX"),
    TORONTOBLUEJAYS(141, "tor", "Toronto Blue Jays", "TOR"),
    WASHINGTONNATIONALS(120, "was", "Washington Nationals", "WAS"),
    FREEAGENT(1, "fa", "Free Agency", "FA"),
    ;

    private final int id;
    private final String name;
    private final String abbreviation;
    private final String mlbAbbreviation;

    private MLBTeam(int id, String mlbAbbreviation, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.mlbAbbreviation = mlbAbbreviation;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getMlbAbbreviation() {
        return mlbAbbreviation;
    }
}
