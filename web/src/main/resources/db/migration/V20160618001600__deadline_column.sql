USE homer;

ALTER TABLE minor_league_picks
CHANGE deadlineUtc deadlineUTC DATETIME;
ALTER TABLE history_minor_league_picks
CHANGE deadlineUtc deadlineUTC DATETIME;

ALTER TABLE vultures
CHANGE expirationDateUTC deadlineUTC DATETIME;
ALTER TABLE history_vultures
CHANGE expirationDateUTC deadlineUTC DATETIME;

ALTER TABLE free_agent_auctions
CHANGE expirationDateUTC deadlineUTC DATETIME;
ALTER TABLE history_free_agent_auctions
CHANGE expirationDateUTC deadlineUTC DATETIME;
