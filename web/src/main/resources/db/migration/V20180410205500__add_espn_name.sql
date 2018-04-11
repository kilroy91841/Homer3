USE homer;

ALTER TABLE players
ADD COLUMN espnName VARCHAR(50);

ALTER TABLE history_players
ADD COLUMN espnName VARCHAR(50);