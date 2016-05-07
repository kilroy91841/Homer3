USE homer;

DROP TABLE IF EXISTS ref_vulture_status;
CREATE TABLE IF NOT EXISTS ref_vulture_status (
    id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_vulture_status (id, name)
VALUES
(1, "In Progress"),
(2, "Fixed"),
(3, "Successful"),
(4, "Invalid"),
(5, "Error");

DROP TABLE IF EXISTS vultures;
CREATE TABLE IF NOT EXISTS vultures (
    id BIGINT NOT NULL AUTO_INCREMENT,
    playerId BIGINT NOT NULL,
    dropPlayerId BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    expirationDateUTC DATETIME NOT NULL,
    vultureStatus INT NOT NULL,
    isCommisionerVulture TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (dropPlayerId) REFERENCES players (id),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (vultureStatus) REFERENCES ref_vulture_status (id)
);

ALTER TABLE vultures AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_vultures;
CREATE TABLE IF NOT EXISTS history_vultures (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    playerId BIGINT NOT NULL,
    dropPlayerId BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    expirationDateUTC DATETIME NOT NULL,
    vultureStatus INT NOT NULL,
    isCommisionerVulture TINYINT(1) NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_vultures AUTO_INCREMENT = 1;