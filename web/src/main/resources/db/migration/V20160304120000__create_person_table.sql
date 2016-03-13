USE homer3;

CREATE TABLE ref_position (
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
(13, 'DL', NULL, NULL);

CREATE TABLE ref_mlb_team (
    id INT NOT NULL,
    name VARCHAR(30) NOT NULL,
    abbreviation VARCHAR(3) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_mlb_team (id, name, abbreviation)
VALUES
(1, 'New York Yankees', 'NYY'),
(2, 'Boston Red Sox', 'BOS'),
(3, 'Baltimore Orioles', 'BAL'),
(4, 'Toronto Blue Jays', 'TOR'),
(5, 'Tampa Bay Rays', 'TB'),
(6, 'Kansas City Royals', 'KC'),
(7, 'Detroit Tigers', 'DET'),
(8, 'Chicago White Sox', 'CHW'),
(9, 'Cleveland Indians', 'CLE'),
(10, 'Minnesota Twins', 'MIN'),
(11, 'Texas Rangers', 'TEX'),
(12, 'Oakland Athletics', 'OAK'),
(13, 'Seattle Marines', 'SEA'),
(14, 'Los Angeles Angels', 'LAA'),
(15, 'Houston Astros', 'HOU'),
(16, 'New York Mets', 'NYM'),
(17, 'Washington Nationals', 'WAS'),
(18, 'Atlanta Braves', 'ATL'),
(19, 'Miami Marlins', 'MIA'),
(20, 'Philadelphia Phillies', 'PHI'),
(21, 'St. Louis Cardinals', 'STL'),
(22, 'Chicago Cubs', 'CHC'),
(23, 'Pittsburgh Pirates', 'PIT'),
(24, 'Milwaukee Brewers', 'MIL'),
(25, 'Cincinnati Reds', 'CIN'),
(26, 'Los Angeles Dodgers', 'LAD'),
(27, 'Colorado Rockies', 'COL'),
(28, 'San Francisco Giants', 'SF'),
(29, 'San Diego Padres', 'SD'),
(30, 'Arizona Diamondbacks', 'ARI');

CREATE TABLE team (
    id INT NOT NULL,
    name VARCHAR(30) NOT NULL,
    abbreviation VARCHAR(5) NOT NULL,
    owner1 VARCHAR(20) NOT NULL,
    owner2 VARCHAR(20),
    createdDateUTC BIGINT NOT NULL,
    updatedDateUTC BIGINT NOT NULL,
    PRIMARY KEY (id)
);

SELECT UNIX_TIMESTAMP(now()) INTO @now;

INSERT INTO team (id, name, abbreviation, owner1, owner2, createdDateUTC, updatedDateUTC)
VALUES
(1, 'Jeurys Prudence', 'RBG', 'Ari Golub', NULL, @now, @now),
(2, 'Ashley Schaeffer BMW', 'BMW', 'Brian McGlade', NULL, @now, @now),
(3, 'Tulo Window Tulo Wall', 'TULO', 'Jeff Berk', NULL, @now, @now),
(4, 'Mesoraco\'s Modern Life', 'SIDO', 'David Sidorov', NULL, @now, @now),
(5, 'Brooklyn No Sox', 'MAD', 'Mike Davey', NULL, @now, @now),
(6, 'Golubagos Islands', 'ARI', 'Darren Grove', NULL, @now, @now),
(7, 'Ya Killin Me Smalls', 'YKMS', 'Tim Swierad', NULL, @now, @now),
(8, 'Correaola Correayons', 'LZRD', 'Brendan Lazarus', 'Matt Reed', @now, @now),
(9, 'Can\'t Miss Prospects', 'CMP', 'Jacob Gerber', 'Matt Shapiro', @now, @now),
(10, 'Pablo Sanchez and Co', 'PABL', 'Kevin Simon', NULL, @now, @now),
(11, 'Meow Meow I\'m The Cat Tank', 'TANK', 'Antoine Gobin', NULL, @now, @now),
(12, 'Kershawshank Redemption II', 'RED', 'Ian Tasso', NULL, @now, @now);

CREATE TABLE player (
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL,
    firstName VARCHAR(20) NOT NULL,
    lastName VARCHAR(20) NOT NULL,
    mlbTeamId INT NOT NULL,
    positionId INT NOT NULL,
    createdDateUTC BIGINT NOT NULL,
    updatedDateUTC BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (positionId) REFERENCES ref_position (id),
    FOREIGN KEY (mlbTeamId) REFERENCES ref_mlb_team (id)
);

CREATE TABLE player_season (
    id BIGINT NOT NULL AUTO_INCREMENT,
    season INT NOT NULL,
    playerId BIGINT NOT NULL,
    teamId INT NOT NULL,
    positionId INT NOT NULL,
    keeperSeason INT NOT NULL DEFAULT 0,
    salary INT NOT NULL DEFAULT 0,
    keeperTeamId INT,
    isMinorLeaguer TINYINT(1) NOT NULL DEFAULT 0,
    createdDateUTC BIGINT NOT NULL,
    updatedDateUTC BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (positionId) REFERENCES ref_position (id),
    FOREIGN KEY (teamId) REFERENCES team (id)
);

CREATE TABLE test (
	id BIGINT NOT NULL AUTO_INCREMENT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    count INT NOT NULL,
    name VARCHAR(100),
    longObject BIGINT NULL,
    myBool TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);