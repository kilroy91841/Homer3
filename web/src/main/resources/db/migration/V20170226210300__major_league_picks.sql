USE homer;

DROP TABLE IF EXISTS major_league_picks;
CREATE TABLE IF NOT EXISTS major_league_picks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    teamId BIGINT NOT NULL,
    playerId BIGINT NOT NULL,
    amount INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (playerId) REFERENCES players (id)
);