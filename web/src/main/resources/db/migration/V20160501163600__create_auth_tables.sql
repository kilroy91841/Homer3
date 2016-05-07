USE homer;

DROP TABLE IF EXISTS session_tokens;
CREATE TABLE IF NOT EXISTS session_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(32) NOT NULL,
    teamId BIGINT NOT NULL,
    userName VARCHAR(50) NOT NULL,
    expirationDateUTC DATETIME NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

ALTER TABLE session_tokens AUTO_INCREMENT = 1;