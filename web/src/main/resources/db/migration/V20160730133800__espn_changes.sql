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
    date DATE NOT NULL,
    playerId BIGINT NOT NULL,
    gameId VARCHAR(50) NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    scoringPeriodId INT NOT NULL,
    walks INT NOT NULL,
    atBats INT NOT NULL,
    homeRuns INT NOT NULL,
    runs INT NOT NULL,
    rbi INT NOT NULL,
    stolenBases INT NOT NULL,
    hitByPitches INT NOT NULL,
    sacFlies INT NOT NULL,
    totalBases INT NOT NULL,
    strikeouts INT NOT NULL,
    wins INT NOT NULL,
    saves INT NOT NULL,
    inningsPitched DOUBLE NOT NULL,
    hits INT NOT NULL,
    earnedRuns INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (playerId, date, gameId),
    FOREIGN KEY (playerId) references players (id),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (fantasyPosition) REFERENCES ref_position (id)
);

DROP TABLE IF EXISTS history_player_dailies;
CREATE TABLE IF NOT EXISTS history_player_dailies (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    date DATE NOT NULL,
    playerId BIGINT NOT NULL,
    gameId VARCHAR(50) NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    scoringPeriodId INT NOT NULL,
    walks INT NOT NULL,
    atBats INT NOT NULL,
    homeRuns INT NOT NULL,
    runs INT NOT NULL,
    rbi INT NOT NULL,
    stolenBases INT NOT NULL,
    hitByPitches INT NOT NULL,
    sacFlies INT NOT NULL,
    totalBases INT NOT NULL,
    strikeouts INT NOT NULL,
    wins INT NOT NULL,
    saves INT NOT NULL,
    inningsPitched DOUBLE NOT NULL,
    hits INT NOT NULL,
    earnedRuns INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_player_dailies AUTO_INCREMENT = 1;