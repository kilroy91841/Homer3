USE homer;

UPDATE history_draft_dollars
SET tradeId = 6
WHERE historyId = 4;

UPDATE history_draft_dollars
SET tradeId = 7
WHERE historyId = 7;

UPDATE history_draft_dollars
SET tradeId = 8
WHERE historyId in (1, 8);

UPDATE history_draft_dollars
SET tradeId = 9
WHERE historyId in (31, 36);

UPDATE history_draft_dollars
SET tradeId = 10
WHERE historyId in (35, 28);

UPDATE history_draft_dollars
SET tradeId = 11
WHERE historyId in (73, 74);

UPDATE history_draft_dollars
SET tradeId = 12
WHERE historyId in (75, 76, 77, 78);

UPDATE history_draft_dollars
SET tradeId = 13
WHERE historyId in (79, 80);

UPDATE history_draft_dollars
SET tradeId = 14
WHERE historyId in (81, 82, 83, 84);

UPDATE history_draft_dollars
SET tradeId = 15
WHERE historyId in (85, 86);

UPDATE history_draft_dollars
SET tradeId = 16
WHERE historyId in (87, 88);

UPDATE history_draft_dollars
SET tradeId = 22
WHERE historyId in (89, 90, 91, 92);

UPDATE history_draft_dollars
SET tradeId = 23
WHERE historyId in (93, 94);

UPDATE history_draft_dollars
SET tradeId = 24
WHERE historyId in (95, 96);

UPDATE history_draft_dollars
SET tradeId = 26
WHERE historyId in (97, 98, 99, 100);

UPDATE history_draft_dollars
SET tradeId = 28
WHERE historyId in (101, 102, 103, 104);

UPDATE history_draft_dollars
SET tradeId = 29
WHERE historyId in (105, 106);

UPDATE history_draft_dollars
SET tradeId = 30
WHERE historyId in (107, 108);

UPDATE history_draft_dollars
SET tradeId = 31
WHERE historyId in (109, 110);

UPDATE history_draft_dollars
SET tradeId = 33
WHERE historyId in (111, 112, 113, 114);

UPDATE history_draft_dollars
SET tradeId = 36
WHERE historyId in (115, 116);

UPDATE history_draft_dollars
SET tradeId = 37
WHERE historyId in (117, 118);

UPDATE history_draft_dollars
SET tradeId = 38
WHERE historyId in (119, 120);

UPDATE history_draft_dollars
SET tradeId = 39
WHERE historyId in (121, 122, 123, 124);

UPDATE history_draft_dollars
SET tradeId = 41
WHERE historyId in (125, 126);

UPDATE history_draft_dollars
SET tradeId = 42
WHERE historyId in (127, 128, 129, 130);

UPDATE history_draft_dollars
SET tradeId = 43
WHERE historyId in (131, 132, 133, 134);

UPDATE history_draft_dollars
SET tradeId = 44
WHERE historyId in (135, 136);

UPDATE history_draft_dollars
SET tradeId = 45
WHERE historyId in (137, 138);

UPDATE history_draft_dollars
SET tradeId = 46
WHERE historyId in (139, 140);

UPDATE history_draft_dollars
SET tradeId = 47
WHERE historyId in (141, 142);

REPLACE history_draft_dollars (historyId, id, teamId, season, draftDollarType, amount, tradeId, createdDateUTC, updatedDateUTC, historyCreatedDateUTC, isDeleted)
VALUES
(-100, 4, 4, 2017, 1, 253, null, NOW(), NOW(), NOW(), 0),
(-99, 10, 10, 2017, 1, 304, null, NOW(), NOW(), NOW(), 0),
(-98, 4, 4, 2017, 1, 246, 1, NOW(), NOW(), NOW(), 0),
(-97, 10, 10, 2017, 1, 311, 1, NOW(), NOW(), NOW(), 0),

(-96, 5, 5, 2017, 1, 257, null, NOW(), NOW(), NOW(), 0),
(-95, 12, 12, 2017, 1, 267, null, NOW(), NOW(), NOW(), 0),
(-94, 5, 5, 2017, 1, 227, 2, NOW(), NOW(), NOW(), 0),
(-93, 12, 12, 2017, 1, 297, 2, NOW(), NOW(), NOW(), 0),

(-92, 4, 4, 2017, 1, 228, 3, NOW(), NOW(), NOW(), 0),
(-91, 12, 12, 2017, 1, 315, 3, NOW(), NOW(), NOW(), 0),

(-90, 6, 6, 2017, 1, 271, null, NOW(), NOW(), NOW(), 0),
(-89, 12, 12, 2017, 1, 332, 4, NOW(), NOW(), NOW(), 0),
(-88, 6, 6, 2017, 1, 254, 4, NOW(), NOW(), NOW(), 0),

(-87, 10, 10, 2017, 1, 306, 5, NOW(), NOW(), NOW(), 0),
(-86, 3, 3, 2017, 1, 220, null, NOW(), NOW(), NOW(), 0),
(-85, 3, 3, 2017, 1, 225, 5, NOW(), NOW(), NOW(), 0),

(-84, 4, 4, 2017, 1, 306, 6, NOW(), NOW(), NOW(), 0),
(-83, 3, 3, 2017, 1, 220, null, NOW(), NOW(), NOW(), 0),
(-82, 3, 3, 2017, 1, 225, 6, NOW(), NOW(), NOW(), 0),

(-81, 6, 6, 2017, 1, 249, 7, NOW(), NOW(), NOW(), 0);

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 1 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 1 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 1 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 1 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 1 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 1 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 1 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 1 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 2 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 2 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 2 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 2 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 2 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 2 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 2 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 2 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 3 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 3 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 3 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 3 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 3 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 3 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 3 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 3 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 4 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 4 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 4 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 4 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 4 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 4 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 4 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 4 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 5 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 5 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 5 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 5 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 5 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 5 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 5 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 5 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 6 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 6 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 6 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 6 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 6 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 6 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 6 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 6 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 7 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 7 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 7 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 7 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 7 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 7 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 7 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 7 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 8 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 8 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 8 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 8 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 8 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 8 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 8 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 8 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 9 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 9 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 9 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 9 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 9 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 9 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 9 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 9 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 10 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 10 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 10 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 10 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 10 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 10 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 10 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 10 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 11 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 11 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 11 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 11 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 11 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 11 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 11 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 11 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 12 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 12 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 12 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 12 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 12 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 12 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 12 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 12 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2018 and teamId = 12 and draftDollarType = 2
order by historyId desc
limit 1)
WHERE season = 2018 and teamId = 12 and draftDollarType = 2;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 5 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 5 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 10 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 10 and draftDollarType = 1;

UPDATE draft_dollars
SET tradeId = (
select tradeId from history_draft_dollars
where season = 2017 and teamId = 11 and draftDollarType = 1
order by historyId desc
limit 1)
WHERE season = 2017 and teamId = 11 and draftDollarType = 1;