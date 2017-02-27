USE homer;

ALTER TABLE draft_dollars
ADD COLUMN draftedPlayerId BIGINT AFTER expired;
ALTER TABLE history_draft_dollars
ADD COLUMN draftedPlayerId BIGINT AFTER expired;

ALTER TABLE player_seasons
ADD COLUMN draftTeamId BIGINT AFTER keeperTeamId;
ALTER TABLE history_player_seasons
ADD COLUMN draftTeamId BIGINT AFTER keeperTeamId;

