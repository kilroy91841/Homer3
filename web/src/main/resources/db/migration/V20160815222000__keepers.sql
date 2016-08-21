USE homer;

DROP TABLE IF EXISTS keepers;
CREATE TABLE IF NOT EXISTS keepers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    teamId BIGINT NOT NULL,
    playerId BIGINT NOT NULL,
    keeperSeason INT NOT NULL,
    salary INT NOT NULL,
    isMinorLeaguer TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

ALTER TABLE keepers AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_keepers;
CREATE TABLE IF NOT EXISTS history_keepers (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    season INT NOT NULL,
    teamId BIGINT NOT NULL,
    playerId BIGINT NOT NULL,
    keeperSeason INT NOT NULL,
    salary INT NOT NULL,
    isMinorLeaguer TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_keepers AUTO_INCREMENT = 1;