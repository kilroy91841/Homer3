USE homer;

DELIMITER //

DROP PROCEDURE IF EXISTS homer.add_draft_dollar //
CREATE PROCEDURE add_draft_dollar(
  in_teamId BIGINT,
  in_season INT,
  in_draftDollarName VARCHAR(20),
  in_amount INT
)
READS SQL DATA
  BEGIN
    DECLARE doesExist TINYINT(1);
    DECLARE draftDollarTypeId INT;
    DECLARE now DATETIME;
    DECLARE newId BIGINT;

    SELECT id INTO draftDollarTypeId
    FROM ref_draft_dollar_type
    WHERE name = in_draftDollarName;

    SELECT COUNT(*) INTO doesExist
    FROM draft_dollars
    WHERE draftDollarType = draftDollarTypeId
    AND teamId = in_teamId
    AND season = in_season;

    IF doesExist = 0 THEN
        SET now = UTC_TIMESTAMP();

        INSERT INTO draft_dollars (teamId, season, draftDollarType, amount, createdDateUTC, updatedDateUTC)
        VALUES                    (in_teamId, in_season, draftDollarTypeId, in_amount, now, now);

        SET newId = LAST_INSERT_ID();

        INSERT INTO history_draft_dollars (id, teamId, season, draftDollarType, amount, createdDateUTC, updatedDateUTC, historyCreatedDateUTC, isDeleted)
        SELECT id, teamId, season, draftDollarType, amount, createdDateUTC, updatedDateUTC, now, 0
        FROM draft_dollars
        WHERE id = newId;

    END IF ;
END //

DROP PROCEDURE IF EXISTS homer.add_draft_dollar_trade //
CREATE PROCEDURE add_draft_dollar_trade(
  in_trade_season INT,
  in_fromTeamId BIGINT,
  in_toTeamId BIGINT,
  in_draftDollarName VARCHAR(20),
  in_season INT,
  in_amount INT,
  in_date TIMESTAMP
 )
 READS SQL DATA
    BEGIN
        DECLARE draftDollarTypeId INT;
        DECLARE draftDollarId BIGINT;
        DECLARE now DATETIME;
        DECLARE newId BIGINT;

        SELECT id INTO draftDollarTypeId
        FROM ref_draft_dollar_type
        WHERE name = in_draftDollarName;

        SELECT id INTO draftDollarId
        FROM draft_dollars
        WHERE teamId = in_fromTeamId
        AND season = in_season
        AND draftDollarType = draftDollarTypeId;

        SET now = UTC_TIMESTAMP();

        INSERT INTO trades (season, team1Id, team2Id, tradeDate, createdDateUTC, updatedDateUTC)
        VALUES              (in_trade_season, in_fromTeamId, in_toTeamId, in_date, now, now);

        SET newId = LAST_INSERT_ID();

        INSERT INTO trade_elements (tradeId, teamFromId, teamToId, draftDollarId, draftDollarAmount, createdDateUTC, updatedDateUTC)
        VALUES                     (newId, in_fromTeamId, in_toTeamId, draftDollarId, in_amount, now, now);
     END //

DROP PROCEDURE IF EXISTS homer.add_minor_league_pick //
CREATE PROCEDURE add_minor_league_pick(
  in_season INT,
  in_originalTeamId BIGINT,
  in_owningTeamId BIGINT,
  in_swapTeamId BIGINT,
  in_round INT,
  in_note VARCHAR(150)
)
READS SQL DATA
    BEGIN
        DECLARE doesExist TINYINT(1);
        DECLARE now DATETIME;
        DECLARE newId BIGINT;

        SELECT COUNT(*) INTO doesExist
        FROM minor_league_picks
        WHERE originalTeamId = in_originalTeamId
        AND round = in_round
        AND season = in_season;

        IF doesExist = 0 THEN
            SET now = UTC_TIMESTAMP();

            INSERT INTO minor_league_picks (season, originalTeamId, owningTeamId, swapTeamId, round, note, createdDateUTC, updatedDateUTC)
            VALUES                         (in_season, in_originalTeamId, in_owningTeamId, in_swapTeamId, in_round, in_note, now, now);

            SET newId = LAST_INSERT_ID();

            INSERT INTO history_minor_league_picks (id, season, originalTeamId, owningTeamId, swapTeamId, round, note, createdDateUTC, updatedDateUTC, historyCreatedDateUTC)
            SELECT id, season, originalTeamId, owningTeamId, swapTeamId, round, note, createdDateUTC, updatedDateUTC, now
            FROM minor_league_picks
            WHERE id = newId;
        END IF;
END //
DELIMITER ;

CALL add_draft_dollar(1, 2017, 'MLBAUCTION', 253);
CALL add_draft_dollar(2, 2017, 'MLBAUCTION', 232);
CALL add_draft_dollar(3, 2017, 'MLBAUCTION', 225);
CALL add_draft_dollar(4, 2017, 'MLBAUCTION', 216);
CALL add_draft_dollar(5, 2017, 'MLBAUCTION', 227);
CALL add_draft_dollar(6, 2017, 'MLBAUCTION', 249);
CALL add_draft_dollar(7, 2017, 'MLBAUCTION', 265);
CALL add_draft_dollar(8, 2017, 'MLBAUCTION', 275);
CALL add_draft_dollar(9, 2017, 'MLBAUCTION', 248);
CALL add_draft_dollar(10, 2017, 'MLBAUCTION', 306);
CALL add_draft_dollar(11, 2017, 'MLBAUCTION', 292);
CALL add_draft_dollar(12, 2017, 'MLBAUCTION', 332);

CALL add_draft_dollar(1, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(2, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(3, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(4, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(5, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(6, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(7, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(8, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(9, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(10, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(11, 2017, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(12, 2017, 'FREEAGENTAUCTION', 100);

CALL add_draft_dollar(1, 2018, 'MLBAUCTION', 260);
CALL add_draft_dollar(2, 2018, 'MLBAUCTION', 236);
CALL add_draft_dollar(3, 2018, 'MLBAUCTION', 235);
CALL add_draft_dollar(4, 2018, 'MLBAUCTION', 262);
CALL add_draft_dollar(5, 2018, 'MLBAUCTION', 260);
CALL add_draft_dollar(6, 2018, 'MLBAUCTION', 275);
CALL add_draft_dollar(7, 2018, 'MLBAUCTION', 225);
CALL add_draft_dollar(8, 2018, 'MLBAUCTION', 260);
CALL add_draft_dollar(9, 2018, 'MLBAUCTION', 260);
CALL add_draft_dollar(10, 2018, 'MLBAUCTION', 289);
CALL add_draft_dollar(11, 2018, 'MLBAUCTION', 263);
CALL add_draft_dollar(12, 2018, 'MLBAUCTION', 295);

CALL add_draft_dollar(1, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(2, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(3, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(4, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(5, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(6, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(7, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(8, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(9, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(10, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(11, 2018, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(12, 2018, 'FREEAGENTAUCTION', 100);

CALL add_draft_dollar(1, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(2, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(3, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(4, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(5, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(6, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(7, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(8, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(9, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(10, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(11, 2019, 'MLBAUCTION', 260);
CALL add_draft_dollar(12, 2019, 'MLBAUCTION', 260);

CALL add_draft_dollar(1, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(2, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(3, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(4, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(5, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(6, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(7, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(8, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(9, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(10, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(11, 2019, 'FREEAGENTAUCTION', 100);
CALL add_draft_dollar(12, 2019, 'FREEAGENTAUCTION', 100);

CALL add_draft_dollar_trade(2016, 4, 10, 'MLBAUCTION', 2017, 10, '2016-03-16 12:00:00');
CALL add_draft_dollar_trade(2016, 5, 12, 'MLBAUCTION', 2017, 30, '2016-03-16 12:01:00');
CALL add_draft_dollar_trade(2016, 4, 12, 'MLBAUCTION', 2017, 18, '2016-03-16 12:02:00');
CALL add_draft_dollar_trade(2016, 6, 12, 'MLBAUCTION', 2017, 17, '2016-03-16 12:03:00');
CALL add_draft_dollar_trade(2016, 10, 3, 'MLBAUCTION', 2017, 5, '2016-03-16 12:04:00');
CALL add_draft_dollar_trade(2016, 4, 2, 'MLBAUCTION', 2017, 12, '2016-03-16 12:05:00');
CALL add_draft_dollar_trade(2016, 6, 7, 'MLBAUCTION', 2017, 5, '2016-03-16 12:06:00');
CALL add_draft_dollar_trade(2016, 1, 8, 'MLBAUCTION', 2017, 15, '2016-03-16 12:07:00');
CALL add_draft_dollar_trade(2016, 7, 12, 'MLBAUCTION', 2018, 35, '2016-03-16 12:08:00');
CALL add_draft_dollar_trade(2016, 4, 11, 'MLBAUCTION', 2018, 3, '2016-03-16 12:09:00');

CALL add_minor_league_pick(2016, 1, 1, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 1, 2, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 1, 1, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 1, 1, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 1, 1, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 1, 1, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 1, 1, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 1, 1, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 1, 1, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 1, 1, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 1, 1, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 1, 1, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 1, 1, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 1, 1, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 1, 1, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 2, 1, 10, 1, NULL);
CALL add_minor_league_pick(2016, 2, 2, 10, 2, NULL);
CALL add_minor_league_pick(2016, 2, 5, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 2, 2, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 2, 6, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 2, 2, 10, 1, NULL);
CALL add_minor_league_pick(2017, 2, 2, 10, 2, NULL);
CALL add_minor_league_pick(2017, 2, 2, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 2, 2, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 2, 2, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 2, 2, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 2, 2, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 2, 2, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 2, 2, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 2, 2, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 3, 3, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 3, 4, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 3, 3, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 3, 3, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 3, 3, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 3, 3, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 3, 3, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 3, 3, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 3, 3, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 3, 3, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 3, 3, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 3, 3, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 3, 3, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 3, 3, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 3, 3, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 4, 4, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 4, 4, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 4, 4, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 4, 4, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 4, 4, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 4, 4, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 4, 4, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 4, 4, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 4, 4, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 4, 4, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 4, 4, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 4, 4, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 4, 4, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 4, 4, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 4, 4, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 5, 5, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 5, 5, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 5, 5, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 5, 5, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 5, 5, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 5, 5, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 5, 5, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 5, 5, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 5, 5, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 5, 5, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 5, 5, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 5, 5, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 5, 5, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 5, 5, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 5, 5, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 6, 6, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 6, 6, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 6, 6, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 6, 6, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 6, 6, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 6, 6, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 6, 6, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 6, 6, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 6, 6, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 6, 6, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 6, 6, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 6, 6, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 6, 6, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 6, 6, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 6, 6, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 7, 7, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 7, 7, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 7, 7, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 7, 7, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 7, 7, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 7, 7, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 7, 7, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 7, 7, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 7, 7, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 7, 7, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 7, 7, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 7, 7, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 7, 7, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 7, 7, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 7, 7, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 8, 8, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 8, 8, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 8, 8, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 8, 8, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 8, 8, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 8, 8, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 8, 8, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 8, 8, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 8, 8, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 8, 8, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 8, 8, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 8, 8, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 8, 8, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 8, 8, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 8, 8, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 9, 9, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 9, 9, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 9, 9, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 9, 9, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 9, 9, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 9, 9, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 9, 9, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 9, 9, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 9, 9, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 9, 9, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 9, 9, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 9, 9, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 9, 9, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 9, 9, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 9, 9, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 10, 10, 12, 1, 'If owned by team 12, swap can take place only between this pick and 11/1 (originalTeam/round).');
CALL add_minor_league_pick(2016, 10, 10, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 10, 10, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 10, 10, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 10, 10, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 10, 10, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 10, 10, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 10, 10, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 10, 10, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 10, 10, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 10, 10, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 10, 10, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 10, 10, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 10, 10, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 10, 10, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 11, 12, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 11, 11, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 11, 11, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 11, 11, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 11, 11, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 11, 11, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 11, 11, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 11, 11, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 11, 11, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 11, 11, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 11, 11, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 11, 11, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 11, 11, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 11, 11, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 11, 11, NULL, 5, NULL);

CALL add_minor_league_pick(2016, 12, 12, NULL, 1, NULL);
CALL add_minor_league_pick(2016, 12, 12, NULL, 2, NULL);
CALL add_minor_league_pick(2016, 12, 12, NULL, 3, NULL);
CALL add_minor_league_pick(2016, 12, 12, NULL, 4, NULL);
CALL add_minor_league_pick(2016, 12, 12, NULL, 5, NULL);
CALL add_minor_league_pick(2017, 12, 12, NULL, 1, NULL);
CALL add_minor_league_pick(2017, 12, 12, NULL, 2, NULL);
CALL add_minor_league_pick(2017, 12, 12, NULL, 3, NULL);
CALL add_minor_league_pick(2017, 12, 12, NULL, 4, NULL);
CALL add_minor_league_pick(2017, 12, 12, NULL, 5, NULL);
CALL add_minor_league_pick(2018, 12, 12, NULL, 1, NULL);
CALL add_minor_league_pick(2018, 12, 12, NULL, 2, NULL);
CALL add_minor_league_pick(2018, 12, 12, NULL, 3, NULL);
CALL add_minor_league_pick(2018, 12, 12, NULL, 4, NULL);
CALL add_minor_league_pick(2018, 12, 12, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 1, 1, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 1, 1, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 1, 1, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 1, 1, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 1, 1, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 2, 2, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 2, 2, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 2, 2, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 2, 2, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 2, 2, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 3, 3, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 3, 3, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 3, 3, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 3, 3, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 3, 3, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 4, 4, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 4, 4, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 4, 4, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 4, 4, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 4, 4, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 5, 5, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 5, 5, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 5, 5, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 5, 5, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 5, 5, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 6, 6, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 6, 6, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 6, 6, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 6, 6, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 6, 6, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 7, 7, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 7, 7, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 7, 7, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 7, 7, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 7, 7, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 8, 8, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 8, 8, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 8, 8, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 8, 8, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 8, 8, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 9, 9, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 9, 9, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 9, 9, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 9, 9, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 9, 9, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 10, 10, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 10, 10, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 10, 10, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 10, 10, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 10, 10, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 11, 11, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 11, 11, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 11, 11, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 11, 11, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 11, 11, NULL, 5, NULL);

CALL add_minor_league_pick(2019, 12, 12, NULL, 1, NULL);
CALL add_minor_league_pick(2019, 12, 12, NULL, 2, NULL);
CALL add_minor_league_pick(2019, 12, 12, NULL, 3, NULL);
CALL add_minor_league_pick(2019, 12, 12, NULL, 4, NULL);
CALL add_minor_league_pick(2019, 12, 12, NULL, 5, NULL);