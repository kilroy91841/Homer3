USE homer;

CREATE TABLE IF NOT EXISTS history_teams (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    abbreviation VARCHAR(5) NOT NULL,
    owner1 VARCHAR(20) NOT NULL,
    owner2 VARCHAR(20),
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);