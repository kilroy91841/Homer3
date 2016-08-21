USE homer;

ALTER TABLE player_seasons
ADD COLUMN hasRookieStatus TINYINT(1) AFTER isMinorLeaguer;

ALTER TABLE history_player_seasons
ADD COLUMN hasRookieStatus TINYINT(1) AFTER isMinorLeaguer;

UPDATE player_seasons
SET hasRookieStatus = 0
WHERE isMinorLeaguer = 0;

UPDATE player_seasons
SET hasRookieStatus = 1
WHERE isMinorLeaguer = 1;

UPDATE history_player_seasons
SET hasRookieStatus = 0
WHERE isMinorLeaguer = 0;

UPDATE history_player_seasons
SET hasRookieStatus = 1
WHERE isMinorLeaguer = 1;

ALTER TABLE player_seasons
CHANGE COLUMN hasRookieStatus hasRookieStatus TINYINT(1) NOT NULL;

ALTER TABLE history_player_seasons
CHANGE COLUMN hasRookieStatus hasRookieStatus TINYINT(1) NOT NULL;