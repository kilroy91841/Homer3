USE homer;

ALTER TABLE trades
ADD COLUMN tradeStatus INT NOT NULL AFTER season;

ALTER TABLE trades
CHANGE tradeDate proposedDateUTC DATETIME NOT NULL;

ALTER TABLE trades
ADD COLUMN respondedDateUTC DATETIME AFTER proposedDateUTC;

UPDATE trades
SET respondedDateUTC = proposedDateUTC, tradeStatus = 3;

DROP TABLE IF EXISTS history_trades;
CREATE TABLE IF NOT EXISTS history_trades (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    season INT NOT NULL,
    tradeStatus INT NOT NULL,
    team1Id BIGINT NOT NULL,
    team2Id BIGINT NOT NULL,
    proposedDateUTC DATETIME NOT NULL,
    respondedDateUTC DATETIME,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_trades AUTO_INCREMENT = 1;