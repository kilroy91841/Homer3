USE homer;

DROP TABLE IF EXISTS september_standings;
CREATE TABLE IF NOT EXISTS september_standings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    teamId BIGINT NOT NULL,
    points DOUBLE NOT NULL,
    place INT,
    dollarsAwarded INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

ALTER TABLE september_standings AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_september_standings;
CREATE TABLE IF NOT EXISTS history_september_standings (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    season INT NOT NULL,
    teamId BIGINT NOT NULL,
    points DOUBLE NOT NULL,
    place INT,
    dollarsAwarded INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_september_standings AUTO_INCREMENT = 1;

ALTER TABLE draft_dollars
ADD COLUMN septemberStandingId BIGINT AFTER tradeId;

ALTER TABLE history_draft_dollars
ADD COLUMN septemberStandingId BIGINT AFTER tradeId;