USE homer;

DROP TABLE IF EXISTS team;
CREATE TABLE IF NOT EXISTS teams (
    id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    abbreviation VARCHAR(5) NOT NULL,
    owner1 VARCHAR(20) NOT NULL,
    owner2 VARCHAR(20),
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS players;
CREATE TABLE IF NOT EXISTS players (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL,
    firstName VARCHAR(20) NOT NULL,
    lastName VARCHAR(20) NOT NULL,
    mlbTeamId INT NOT NULL,
    position INT NOT NULL,
    mlbPlayerId BIGINT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (position) REFERENCES ref_position (id),
    FOREIGN KEY (mlbTeamId) REFERENCES ref_mlb_team (id)
);

ALTER TABLE players AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_players;
CREATE TABLE IF NOT EXISTS history_players (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    name VARCHAR(40) NOT NULL,
    firstName VARCHAR(20) NOT NULL,
    lastName VARCHAR(20) NOT NULL,
    mlbTeamId INT NOT NULL,
    position INT NOT NULL,
    mlbPlayerId BIGINT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_players AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS player_seasons;
CREATE TABLE IF NOT EXISTS player_seasons (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    playerId BIGINT NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    keeperSeason INT NOT NULL,
    salary INT NOT NULL,
    keeperTeamId BIGINT,
    isMinorLeaguer TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (fantasyPosition) REFERENCES ref_position (id),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

ALTER TABLE player_seasons AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_player_seasons;
CREATE TABLE IF NOT EXISTS history_player_seasons (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    season INT NOT NULL,
    playerId BIGINT NOT NULL,
    teamId BIGINT,
    fantasyPosition INT,
    keeperSeason INT NOT NULL,
    salary INT NOT NULL,
    keeperTeamId BIGINT,
    isMinorLeaguer TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_player_seasons AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS minor_league_picks;
CREATE TABLE IF NOT EXISTS minor_league_picks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    originalTeamId BIGINT NOT NULL,
    owningTeamId BIGINT NOT NULL,
    swapTeamId BIGINT,
    round INT NOT NULL,
    overallPick INT,
    playerId BIGINT,
    isSkipped TINYINT(1),
    note VARCHAR(150),
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (season, originalTeamId, round),
    FOREIGN KEY (originalTeamId) REFERENCES teams (id),
    FOREIGN KEY (owningTeamId) REFERENCES teams (id),
    FOREIGN KEY (swapTeamId) REFERENCES teams (id),
    FOREIGN KEY (playerId) REFERENCES players (id)
);

ALTER TABLE minor_league_picks AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_minor_league_picks;
CREATE TABLE IF NOT EXISTS history_minor_league_picks (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    season INT NOT NULL,
    originalTeamId BIGINT NOT NULL,
    owningTeamId BIGINT NOT NULL,
    swapTeamId BIGINT,
    round INT NOT NULL,
    overallPick INT,
    playerId BIGINT,
    isSkipped TINYINT(1),
    note VARCHAR(150),
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_minor_league_picks AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS draft_dollars;
CREATE TABLE IF NOT EXISTS draft_dollars (
    id BIGINT NOT NULL AUTO_INCREMENT,
    teamId BIGINT NOT NULL,
    season INT NOT NULL,
    draftDollarType INT NOT NULL,
    amount INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (teamId, season, draftDollarType),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (draftDollarType) REFERENCES ref_draft_dollar_type (id)
);

ALTER TABLE draft_dollars AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_draft_dollars;
CREATE TABLE IF NOT EXISTS history_draft_dollars (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    season INT NOT NULL,
    draftDollarType INT NOT NULL,
    amount INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_draft_dollars AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS trades;
CREATE TABLE IF NOT EXISTS trades (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    team1Id BIGINT NOT NULL,
    team2Id BIGINT NOT NULL,
    tradeDate DATETIME NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team1Id) REFERENCES teams (id),
    FOREIGN KEY (team2Id) REFERENCES teams (id)
);

ALTER TABLE trades AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS trade_elements;
CREATE TABLE IF NOT EXISTS trade_elements (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tradeId BIGINT NOT NULL,
    teamFromId BIGINT NOT NULL,
    teamToId BIGINT NOT NULL,
    playerId BIGINT,
    draftDollarId BIGINT,
    draftDollarAmount INT,
    minorLeaguePickId BIGINT,
    swapTrade TINYINT(1),
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (tradeId) REFERENCES trades (id),
    FOREIGN KEY (teamFromId) REFERENCES teams (id),
    FOREIGN KEY (teamToId) REFERENCES teams (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (minorLeaguePickId) REFERENCES minor_league_picks (id),
    FOREIGN KEY (draftDollarId) REFERENCES draft_dollars (id)
);

ALTER TABLE trade_elements AUTO_INCREMENT = 1;
