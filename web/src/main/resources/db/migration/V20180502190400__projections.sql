USE homer;

DROP TABLE IF EXISTS projections;
CREATE TABLE IF NOT EXISTS projections (
    id BIGINT,
    date DATE NOT NULL,
    playerId BIGINT NOT NULL,
    scoringPeriodId INT NOT NULL DEFAULT 0,
    walks INT NOT NULL,
    pitcherWalks INT NOT NULL,
    atBats INT NOT NULL,
    hits INT NOT NULL,
    homeRuns INT NOT NULL,
    runs INT NOT NULL,
    rbi INT NOT NULL,
    stolenBases INT NOT NULL,
    hitByPitches INT NOT NULL,
    sacFlies INT NOT NULL,
    totalBases INT NOT NULL,
    strikeouts INT NOT NULL,
    pitcherStrikeouts INT NOT NULL,
    wins INT NOT NULL,
    saves INT NOT NULL,
    inningsPitched DOUBLE NOT NULL,
    pitcherHits INT NOT NULL,
    earnedRuns INT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (playerId, date),
    FOREIGN KEY (playerId) REFERENCES players (id)
);