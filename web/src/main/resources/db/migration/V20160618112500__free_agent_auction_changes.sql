USE homer;

ALTER TABLE free_agent_auctions
ADD COLUMN denyReason VARCHAR(100) AFTER requestedPlayerName;
ALTER TABLE history_free_agent_auctions
ADD COLUMN denyReason VARCHAR(100) AFTER requestedPlayerName;
ALTER TABLE free_agent_auctions
CHANGE playerId playerId BIGINT NOT NULL;
ALTER TABLE history_free_agent_auctions
CHANGE playerId playerId BIGINT NOT NULL;
ALTER TABLE free_agent_auctions
DROP COLUMN requestedPlayerName;
ALTER TABLE history_free_agent_auctions
DROP COLUMN requestedPlayerName;

INSERT INTO ref_event_status (id, name)
VALUES
(9, "Cancelled");