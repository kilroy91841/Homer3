USE homer;

INSERT INTO ref_position (id, name, grants1, grants2)
VALUES
(15, 'BENCH', NULL, NULL);

ALTER TABLE players
ADD COLUMN espnPlayerId BIGINT AFTER mlbPlayerId;

ALTER TABLE history_players
ADD COLUMN espnPlayerId BIGINT AFTER mlbPlayerId;

DROP TABLE IF EXISTS player_dailies;
CREATE TABLE IF NOT EXISTS player_dailies (
    id BIGINT,
    date DATETIME NOT NULL,
    playerId BIGINT NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    scoringPeriodId INT NOT NULL,
    walks INT,
    atBats INT,
    homeRuns INT,
    runs INT,
    rbi INT,
    stolenBases INT,
    hitByPitches INT,
    sacFlies INT,
    totalBases INT,
    strikeouts INT,
    wins INT,
    saves INT,
    inningsPitched DOUBLE,
    hits INT,
    earnedRuns INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (playerId, date),
    FOREIGN KEY (playerId) references players (id),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (fantasyPosition) REFERENCES ref_position (id)
);

DROP TABLE IF EXISTS history_player_dailies;
CREATE TABLE IF NOT EXISTS history_player_dailies (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    date DATETIME NOT NULL,
    playerId BIGINT NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    scoringPeriodId INT NOT NULL,
    walks INT,
    atBats INT,
    homeRuns INT,
    runs INT,
    rbi INT,
    stolenBases INT,
    hitByPitches INT,
    sacFlies INT,
    totalBases INT,
    strikeouts INT,
    wins INT,
    saves INT,
    inningsPitched DOUBLE,
    hits INT,
    earnedRuns INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_player_dailies AUTO_INCREMENT = 1;