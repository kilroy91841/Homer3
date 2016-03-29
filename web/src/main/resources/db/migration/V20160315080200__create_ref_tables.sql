USE homer;

DROP TABLE IF EXISTS ref_position;
CREATE TABLE IF NOT EXISTS ref_position (
    id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    grants1 INT,
    grants2 INT,
    PRIMARY KEY (id)
);

INSERT INTO ref_position (id, name, grants1, grants2)
VALUES
(1, 'P', NULL, NULL),
(2, 'C', 12, NULL),
(3, '1B', 11, 12),
(4, '2B', 10, 12),
(5, '3B', 11, 12),
(6, 'SS', 10, 12),
(7, 'OF', 12, NULL),
(8, 'DH', 12, NULL),
(9, 'RP', 1, NULL),
(10, '2B/SS', 12, NULL),
(11, '1B/3B', 12, NULL),
(12, 'U', NULL, NULL),
(13, 'DL', NULL, NULL),
(14, 'MIN', NULL, NULL);

DROP TABLE IF EXISTS ref_mlb_team;
CREATE TABLE IF NOT EXISTS ref_mlb_team (
    id INT NOT NULL,
    mlbAbbreviation VARCHAR(3) NOT NULL,
    name VARCHAR(30) NOT NULL,
    abbreviation VARCHAR(3) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_mlb_team (id, mlbAbbreviation, name, abbreviation)
VALUES
(108, "ana", "Los Angeles Angels", "LAA"),
(109, "ari", "Arizona Diamondbacks", "ARI"),
(144, "atl", "Atlanta Braves", "ATL"),
(110, "bal", "Baltimore Orioles", "BAL"),
(111, "bos", "Boston Red Sox", "BOS"),
(112, "chn", "Chicago Cubs", "CHC"),
(113, "cin", "Cincinnati Reds", "CIN"),
(114, "cle", "Cleveland Indians", "CLE"),
(115, "col", "Colorado Rockies", "COL"),
(145, "cha", "Chicago White Sox", "CHW"),
(116, "det", "Detroit Tigers", "DET"),
(117, "hou", "Houston Astros", "HOU"),
(118, "kca", "Kansas City Royals", "KC"),
(119, "lan", "Los Angeles Dodgers", "LAD"),
(146, "mia", "Miami Marlins", "MIA"),
(158, "mil", "Milwaukee Brewers", "MIL"),
(142, "min", "Minnesota Twins", "MIN"),
(121, "nyn", "New York Mets", "NYM"),
(147, "nya", "New York Yankees", "NYY"),
(133, "oak", "Oakland Athletics", "OAK"),
(143, "phi", "Philadelphia Phillies", "PHI"),
(134, "pit", "Pittsburgh Pirates", "PIT"),
(135, "sdn", "San Diego Padres", "SD"),
(136, "sea", "Seattle Mariners", "SEA"),
(137, "sfn", "San Francisco Giants", "SF"),
(138, "sln", "St. Louis Cardinals", "STL"),
(139, "tba", "Tampa Bay Rays", "TB"),
(140, "tex", "Texas Rangers", "TEX"),
(141, "tor", "Toronto Blue Jays", "TOR"),
(120, "was", "Washington Nationals", "WAS");

DROP TABLE IF EXISTS ref_draft_dollar_type;
CREATE TABLE IF NOT EXISTS ref_draft_dollar_type (
    id INT NOT NULL,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_draft_dollar_type (id, name)
VALUES
(1, 'MLBAUCTION'),
(2, 'FREEAGENTAUCTION');