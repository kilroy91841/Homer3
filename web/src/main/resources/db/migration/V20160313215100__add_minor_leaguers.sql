USE homer3;

INSERT INTO ref_position (id, name, grants1, grants2)
VALUES
(14, 'MIN', NULL, NULL);

CALL add_player('Aaron', 'Judge', 1, 1, 0, 0, 2016, 1, 'NYY', 7, 14);
CALL add_player('Rafael', 'Devers', 1, 1, 0, 0, 2016, 1, 'BOS', 5, 14);
CALL add_player('Jesse', 'Winker', 1, 1, 0, 0, 2016, 1, 'CIN', 7, 14);
CALL add_player('Joey', 'Gallo', 1, 1, 0, 0, 2016, 1, 'TEX', 5, 14);
CALL add_player('Dylan', 'Bundy', 1, 1, 0, 0, 2016, 1, 'BAL', 1, 14);
CALL add_player('Lucas', 'Giolito', 1, 1, 0, 0, 2016, 1, 'WAS', 1, 14);
CALL add_player('Tyler', 'Glasnow', 1, 1, 0, 0, 2016, 1, 'PIT', 1, 14);
CALL add_player('Billy', 'McKinney', 1, 1, 0, 0, 2016, 1, 'CHC', 7, 14);
CALL add_player('Julio', 'Urias', 1, 1, 0, 0, 2016, 1, 'LAD', 1, 14);
CALL add_player('Gleyber', 'Torres', 1, 1, 0, 0, 2016, 1, 'CHC', 6, 14);
