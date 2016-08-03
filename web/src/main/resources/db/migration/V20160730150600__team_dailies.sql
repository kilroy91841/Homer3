USE homer;

DROP TABLE IF EXISTS team_dailies;
CREATE TABLE IF NOT EXISTS team_dailies (
    id BIGINT,
    date DATE NOT NULL,
    teamId BIGINT NOT NULL,
    scoringPeriodId INT NOT NULL,
    walks INT NOT NULL,
    pitcherWalks INT NOT NULL,
    atBats INT NOT NULL,
    hits INT NOT NULL,
    homeRuns INT NOT NULL,
    runs INT NOT NULL,
    rbi INT NOT NULL,
    stolenBases INT NOT NULL,
    hitByPitches INT NOT NULL,
    sacFlies INT NOT NULL,
    totalBases INT NOT NULL,
    strikeouts INT NOT NULL,
    pitcherStrikeouts INT NOT NULL,
    wins INT NOT NULL,
    saves INT NOT NULL,
    inningsPitched DOUBLE NOT NULL,
    pitcherHits INT NOT NULL,
    earnedRuns INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (teamId, date),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

DROP TABLE IF EXISTS history_team_dailies;
CREATE TABLE IF NOT EXISTS history_team_dailies (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    date DATE NOT NULL,
    teamId BIGINT NOT NULL,
    scoringPeriodId INT NOT NULL,
    walks INT NOT NULL,
    pitcherWalks INT NOT NULL,
    atBats INT NOT NULL,
    hits INT NOT NULL,
    homeRuns INT NOT NULL,
    runs INT NOT NULL,
    rbi INT NOT NULL,
    stolenBases INT NOT NULL,
    hitByPitches INT NOT NULL,
    sacFlies INT NOT NULL,
    totalBases INT NOT NULL,
    strikeouts INT NOT NULL,
    pitcherStrikeouts INT NOT NULL,
    wins INT NOT NULL,
    saves INT NOT NULL,
    inningsPitched DOUBLE NOT NULL,
    pitcherHits INT NOT NULL,
    earnedRuns INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_team_dailies AUTO_INCREMENT = 1;