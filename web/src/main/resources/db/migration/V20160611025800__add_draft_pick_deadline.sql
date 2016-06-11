USE homer;

ALTER TABLE minor_league_picks
ADD COLUMN deadlineUtc DATETIME AFTER note;
ALTER TABLE history_minor_league_picks
ADD COLUMN deadlineUtc DATETIME AFTER note;