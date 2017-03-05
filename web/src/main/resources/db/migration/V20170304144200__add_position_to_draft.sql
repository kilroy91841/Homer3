USE homer;

ALTER TABLE major_league_picks
ADD COLUMN fantasyPosition INT AFTER amount;

ALTER TABLE major_league_picks
ADD FOREIGN KEY (fantasyPosition) REFERENCES ref_position (id);