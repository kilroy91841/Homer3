USE homer3;

DELIMITER //
DROP PROCEDURE IF EXISTS homer3.add_player //
CREATE PROCEDURE add_player(
  in_firstName VARCHAR(20),
  in_lastName VARCHAR(20),
  in_teamId INT,
  in_salary INT,
  in_keeperSeason INT,
  in_season INT,
  in_isMinorLeaguer TINYINT(1),
  in_mlbTeam VARCHAR(3),
  in_positionId INT,
  in_fantasyPositionId INT
)
READS SQL DATA
  BEGIN

    SET @in_name = CONCAT(in_firstName, ' ', in_lastName);

    SELECT id INTO @mlbTeam FROM ref_mlb_team WHERE in_mlbTeam = abbreviation;

    SELECT UNIX_TIMESTAMP(now()) INTO @now;

    IF NOT EXISTS (select 1 FROM player where name = @in_name) THEN
        INSERT INTO player (name, firstName, lastName, mlbTeamId, positionId, createdDateUtc, updatedDateUtc)
        VALUES
        (@in_name, in_firstName, in_lastName, @mlbTeam, in_positionId, @now, @now);

        SET @id = LAST_INSERT_ID();
    END IF;

    INSERT INTO player_season (season, playerId, teamId, positionId, keeperSeason, salary, isMinorLeaguer, createdDateUTC, updatedDateUTC)
    VALUES
        (in_season, @id, in_teamId, in_fantasyPositionId, in_keeperSeason, in_salary, in_isMinorLeaguer, @now, @now);
  END //
DELIMITER ;

CALL add_player('Joey', 'Votto', 1, 38, 1, '2016', 0, 'CIN', 3, 3);
CALL add_player('Francisco', 'Lindor', 1, 3, 1, '2016', 0, 'CLE', 6, 6);
CALL add_player('Gregory', 'Polanco', 1, 6, 2, '2016', 0, 'PIT', 7, 7);
CALL add_player('Michael', 'Conforto', 1, 3, 1, '2016', 0, 'NYM', 7, 7);
CALL add_player('Jacob', 'DeGrom', 1, 6, 2, '2016', 0, 'NYM', 1, 1);
CALL add_player('Andrew', 'Miller', 1, 3, 1, '2016', 0, 'NYY', 9, 1);
CALL add_player('Jeurys', 'Familia', 1, 3, 1, '2016', 0, 'NYM', 9, 1);
CALL add_player('Lance', 'McCullers', 1, 3, 1, '2016', 0, 'HOU', 1, 1);
CALL add_player('Mark', 'Melancon', 1, 6, 2, '2016', 0, 'PIT', 9, 1);
CALL add_player('Marcus', 'Stroman', 1, 6, 2, '2016', 0, 'TOR', 1, 1);

CALL add_player('Brian', 'Mccann', 2, 28, 1, '2016', 0, 'NYY', 2, 2);
CALL add_player('Jonathon', 'Schoop', 2, 3, 1, '2016', 0, 'BAL', 4, 4);
CALL add_player('Billy', 'Hamilton', 2, 6, 2, '2016', 0, 'CIN', 7, 7);
CALL add_player('Luis', 'Severino', 2, 3, 1, '2016', 0, 'NYY', 1, 1);
CALL add_player('Rougned', 'Odor', 2, 6, 1, '2016', 0, 'TEX', 4, 4);
CALL add_player('Kenley', 'Jansen', 2, 12, 3, '2016', 0, 'LAD', 9, 1);
CALL add_player('Jon', 'Lester', 2, 18, 2, '2016', 0, 'CHC', 1, 1);
CALL add_player('Danny', 'Salazar', 2, 14, 1, '2016', 0, 'CLE', 1, 1);
CALL add_player('Aaron', 'Nola', 2, 3, 1, '2016', 0, 'PHI', 1, 1);
CALL add_player('Wade', 'Davis', 2, 3, 1, '2016', 0, 'KC', 9, 1);

CALL add_player('Russell', 'Martin', 3, 9, 2, '2016', 0, 'TOR', 2, 2);
CALL add_player('Francisco', 'Cervelli', 3, 3, 1, '2016', 0, 'PIT', 2, 2);
CALL add_player('Alex', 'Rodriguez', 3, 8, 1, '2016', 0, 'NYY', 8, 8);
CALL add_player('Corey', 'Dickerson', 3, 6, 2, '2016', 0, 'TB', 7, 7);
CALL add_player('Ken', 'Giles', 3, 4, 1, '2016', 0, 'HOU', 9, 1);
CALL add_player('Zack', 'Greinke', 3, 35, 2, '2016', 0, 'ARI', 1, 1);
CALL add_player('Carlos', 'Rodon', 3, 3, 1, '2016', 0, 'CHW', 1, 1);
CALL add_player('Michael', 'Wacha', 3, 9, 3, '2016', 0, 'STL', 1, 1);
CALL add_player('Eduardo', 'Rodriguez', 3, 3, 1, '2016', 0, 'BOS', 1, 1);

CALL add_player('Jonathan', 'Lucroy', 4, 19, 3, '2016', 0, 'MIL', 2, 2);
CALL add_player('Kolten', 'Wong', 4, 6, 2, '2016', 0, 'STL', 4, 4);
CALL add_player('Hector', 'Olivera', 4, 0, 0, '2016', 0, 'ATL', 5, 5);
CALL add_player('Yasmany', 'Tomas', 4, 3, 1, '2016', 0, 'ARI', 7, 7);
CALL add_player('Billy', 'Burns', 4, 3, 1, '2016', 0, 'OAK', 7, 7);
CALL add_player('Devin', 'Mesoraco', 4, 15, 2, '2016', 0, 'CIN', 2, 2);
CALL add_player('Julio', 'Teheran', 4, 9, 3, '2016', 0, 'ATL', 1, 1);
CALL add_player('Carlos', 'Martinez', 4, 15, 1, '2016', 0, 'STL', 1, 1);
CALL add_player('Kevin', 'Gausman', 4, 14, 1, '2016', 0, 'BAL', 1, 1);

CALL add_player('Blake', 'Swihart', 5, 3, 1, '2016', 0, 'BOS', 2, 2);
CALL add_player('Josh', 'Donaldson', 5, 9, 3, '2016', 0, 'TOR', 5, 5);
CALL add_player('Xander', 'Bogaerts', 5, 6, 2, '2016', 0, 'BOS', 6, 6);
CALL add_player('Nolan', 'Arenado', 5, 11, 2, '2016', 0, 'COL', 5, 5);
CALL add_player('Christian', 'Yelich', 5, 9, 3, '2016', 0, 'MIA', 7, 7);
CALL add_player('Charlie', 'Blackmon', 5, 15, 1, '2016', 0, 'COL', 7, 7);
CALL add_player('Noah', 'Syndergaard', 5, 3, 1, '2016', 0, 'NYM', 1, 1);

CALL add_player('Stephen', 'Vogt', 6, 3, 1, '2016', 0, 'OAK', 2, 2);
CALL add_player('Mike', 'Moustakas', 6, 3, 1, '2016', 0, 'KC', 5, 5);
CALL add_player('Jung Ho', 'Kang', 6, 5, 1, '2016', 0, 'PIT', 4, 4);
CALL add_player('Michael', 'Brantley', 6, 7, 2, '2016', 0, 'CLE', 7, 7);
CALL add_player('Lorenzo', 'Cain', 6, 4, 1, '2016', 0, 'KC', 7, 7);
CALL add_player('Mark', 'Teixeira', 6, 3, 1, '2016', 0, 'NYY', 3, 3);
CALL add_player('Sonny', 'Gray', 6, 9, 3, '2016', 0, 'OAK', 1, 1);
CALL add_player('Matt', 'Harvey', 6, 6, 2, '2016', 0, 'NYM', 1, 1);
CALL add_player('Aaron', 'Sanchez', 6, 3, 1, '2016', 0, 'TOR', 9, 9);
CALL add_player('Adam', 'Ottavino', 6, 3, 1, '2016', 0, 'COL', 9, 9);

CALL add_player('Pablo', 'Sandoval', 7, 19, 1, '2016', 0, 'BOS', 5, 5);
CALL add_player('Adrian', 'Gonzalez', 7, 28, 1, '2016', 0, 'LAD', 3, 3);
CALL add_player('Matt', 'Kemp', 7, 26, 2, '2016', 0, 'SD', 7, 7);
CALL add_player('Melky', 'Cabrera', 7, 6, 2, '2016', 0, 'CHW', 7, 7);
CALL add_player('Jacoby', 'Ellsbury', 7, 36, 3, '2016', 0, 'NYY', 7, 7);
CALL add_player('Dustin', 'Pedroia', 7, 23, 1, '2016', 0, 'BOS', 4, 4);
CALL add_player('Justin', 'Verlander', 7, 9, 1, '2016', 0, 'DET', 1, 1);
CALL add_player('Trevor', 'Rosenthal', 7, 14, 1, '2016', 0, 'STL', 9, 1);

CALL add_player('Welington', 'Castillo', 8, 3, 1, '2016', 0, 'CHC', 2, 2);
CALL add_player('Maikel', 'Franco', 8, 3, 1, '2016', 0, 'PHI', 5, 5);
CALL add_player('Addison', 'Russell', 8, 3, 1, '2016', 0, 'CHC', 6, 6);
CALL add_player('Carlos', 'Correa', 8, 3, 1, '2016', 0, 'HOU', 6, 6);
CALL add_player('Eric', 'Hosmer', 8, 10, 1, '2016', 0, 'KC', 3, 3);
CALL add_player('Jorge', 'Soler', 8, 3, 1, '2016', 0, 'CHC', 7, 7);
CALL add_player('Yasiel', 'Puig', 8, 9, 3, '2016', 0, 'LAD', 7, 7);
CALL add_player('Tyson', 'Ross', 8, 7, 2, '2016', 0, 'SD', 1, 1);
CALL add_player('Gerrit', 'Cole', 8, 9, 3, '2016', 0, 'PIT', 1, 1);
CALL add_player('Jose', 'Fernandez', 8, 9, 3, '2016', 0, 'MIA', 1, 1);

CALL add_player('Travis', 'd\'Arnaud', 9, 6, 2, '2016', 0, 'NYM', 2, 2);
CALL add_player('Lucas', 'Duda', 9, 6, 2, '2016', 0, 'NYM', 3, 3);
CALL add_player('Todd', 'Frazier', 9, 9, 2, '2016', 0, 'CHW', 5, 5);
CALL add_player('A.J.', 'Pollock', 9, 13, 1, '2016', 0, 'ARI', 7, 7);
CALL add_player('Joc', 'Pederson', 9, 3, 1, '2016', 0, 'LAD', 7, 7);
CALL add_player('Kris', 'Bryant', 9, 3, 1, '2016', 0, 'CHC', 5, 5);
CALL add_player('Chris', 'Archer', 9, 9, 3, '2016', 0, 'TB', 1, 1);

CALL add_player('Miguel', 'Sano', 10, 3, 1, '2016', 0, 'MIN', 5, 5);
CALL add_player('Ketel', 'Marte', 10, 3, 1, '2016', 0, 'SEA', 6, 6);
CALL add_player('Dee', 'Gordon', 10, 8, 2, '2016', 0, 'MIA', 4, 4);
CALL add_player('George', 'Springer', 10, 6, 2, '2016', 0, 'HOU', 7, 7);
CALL add_player('Delino', 'Deshields', 10, 3, 1, '2016', 0, 'TEX', 7, 7);
CALL add_player('Mookie', 'Betts', 10, 6, 2, '2016', 0, 'BOS', 7, 7);
CALL add_player('Jaime', 'Garcia', 10, 3, 1, '2016', 0, 'STL', 1, 1);
CALL add_player('Taijuan', 'Walker', 10, 3, 1, '2016', 0, 'SEA', 1, 1);
CALL add_player('Corey', 'Kluber', 10, 12, 2, '2016', 0, 'CLE', 1, 1);
CALL add_player('Carlos', 'Carrasco', 10, 6, 2, '2016', 0, 'CLE', 1, 1);

CALL add_player('J.T.', 'Realmuto', 11, 3, 1, '2016', 0, 'MIA', 2, 2);
CALL add_player('Nelson', 'Cruz', 11, 20, 2, '2016', 0, 'SEA', 7, 7);
CALL add_player('Jose', 'Abreu', 11, 34, 2, '2016', 0, 'CHW', 3, 3);
CALL add_player('Starling', 'Marte', 11, 16, 3, '2016', 0, 'PIT', 7, 7);
CALL add_player('David', 'Ortiz', 11, 22, 1, '2016', 0, 'BOS', 8, 8);
CALL add_player('Jake', 'Arrieta', 11, 6, 2, '2016', 0, 'CHC', 1, 1);
CALL add_player('Patrick', 'Corbin', 11, 4, 1, '2016', 0, 'ARI', 1, 1);
CALL add_player('A.J.', 'Ramos', 11, 3, 1, '2016', 0, 'MIA', 9, 1);
CALL add_player('J.D.', 'Martinez', 11, 6, 2, '2016', 0, 'DET', 7, 7);
CALL add_player('Yu', 'Darvish', 11, 6, 1, '2016', 0, 'TEX', 1, 1);

CALL add_player('Brian', 'Dozier', 12, 16, 2, '2016', 0, 'MIN', 6, 6);
CALL add_player('Brandon', 'Crawford', 12, 3, 1, '2016', 0, 'SF', 6, 6);
CALL add_player('DJ', 'LeMahieu', 12, 3, 1, '2016', 0, 'COL', 4, 4);
CALL add_player('Adam', 'Eaton', 12, 3, 1, '2016', 0, 'CWS', 7, 7);
CALL add_player('Dallas', 'Keuchel', 12, 6, 1, '2016', 0, 'HOU', 1, 1);
CALL add_player('Shawn', 'Tolleson', 12, 3, 1, '2016', 0, 'TEX', 9, 1);
CALL add_player('Raisel', 'Iglesias', 12, 3, 1, '2016', 0, 'CIN', 1, 1);
CALL add_player('Andrew', 'Heaney', 12, 3, 1, '2016', 0, 'LAA', 1, 1);
CALL add_player('Kyle', 'Schwarber', 12, 3, 1, '2016', 0, 'CHC', 2, 2);
CALL add_player('Garret', 'Richards', 12, 6, 2, '2016', 0, 'LAA', 1, 1);

