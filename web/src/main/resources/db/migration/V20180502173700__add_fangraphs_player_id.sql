USE homer;

ALTER TABLE players
ADD COLUMN fangraphsPlayerId BIGINT;

ALTER TABLE history_players
ADD COLUMN fangraphsPlayerId BIGINT;