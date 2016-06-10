USE homer;

DROP TABLE IF EXISTS free_agent_auctions;
CREATE TABLE IF NOT EXISTS free_agent_auctions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    playerId BIGINT,
    season INT NOT NULL,
    expirationDateUTC DATETIME NOT NULL,
    auctionStatus INT NOT NULL,
    requestingTeamId BIGINT NOT NULL,
    requestedPlayerName VARCHAR(30),
    winningTeamId BIGINT,
    winningAmount INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (winningTeamId) REFERENCES teams (id),
    FOREIGN KEY (requestingTeamId) REFERENCES teams (id),
    FOREIGN KEY (auctionStatus) REFERENCES ref_event_status (id)
);

ALTER TABLE free_agent_auctions AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_free_agent_auctions;
CREATE TABLE IF NOT EXISTS history_free_agent_auctions (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    playerId BIGINT,
    season INT NOT NULL,
    expirationDateUTC DATETIME NOT NULL,
    auctionStatus INT NOT NULL,
    requestingTeamId BIGINT NOT NULL,
    requestedPlayerName VARCHAR(30),
    winningTeamId BIGINT,
    winningAmount INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_free_agent_auctions AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS free_agent_auction_bids;
CREATE TABLE IF NOT EXISTS free_agent_auction_bids (
    id BIGINT NOT NULL AUTO_INCREMENT,
    freeAgentAuctionId BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    amount INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (freeAgentAuctionId, teamId),
    FOREIGN KEY (freeAgentAuctionId) REFERENCES free_agent_auctions (id),
    FOREIGN KEY (teamId) REFERENCES teams (id)
);

ALTER TABLE free_agent_auction_bids AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS history_free_agent_auction_bids;
CREATE TABLE IF NOT EXISTS history_free_agent_auction_bids (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
    id BIGINT NOT NULL,
    freeAgentAuctionId BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    amount INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);

ALTER TABLE history_free_agent_auction_bids AUTO_INCREMENT = 1;