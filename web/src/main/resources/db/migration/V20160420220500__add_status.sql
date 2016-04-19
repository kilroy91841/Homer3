USE homer;

DROP TABLE IF EXISTS ref_status;
CREATE TABLE IF NOT EXISTS ref_status (
    id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    abbreviation VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_status (id, name, abbreviation)
VALUES
(1, "Active", "A"),
(2, "Minors", "MIN"),
(3, "Disabled List", "DL"),
(4, "Free Agent", "FA"),
(5, "Unknown", "UNK");

ALTER TABLE player_seasons
ADD COLUMN mlbStatus INT AFTER fantasyPosition;
ALTER TABLE history_player_seasons
ADD COLUMN mlbStatus INT AFTER fantasyPosition;

UPDATE player_seasons
SET mlbStatus = 1;
UPDATE history_player_seasons
SET mlbStatus = 1;

ALTER TABLE player_seasons
MODIFY COLUMN mlbStatus INT NOT NULL;
ALTER TABLE history_player_seasons
MODIFY COLUMN mlbStatus INT NOT NULL;

ALTER TABLE player_seasons
ADD CONSTRAINT FK_player_seasons_mlbStatus FOREIGN KEY (mlbStatus) REFERENCES ref_status (id);

ALTER TABLE player_seasons
ADD COLUMN vulturable TINYINT(1) AFTER isMinorLeaguer;
ALTER TABLE history_player_seasons
ADD COLUMN vulturable TINYINT(1) AFTER isMinorLeaguer;

UPDATE player_seasons
SET vulturable = 0;
UPDATE history_player_seasons
SET vulturable = 0;

ALTER TABLE player_seasons
MODIFY COLUMN vulturable TINYINT(1) NOT NULL;
ALTER TABLE history_player_seasons
MODIFY COLUMN vulturable TINYINT(1) NOT NULL;