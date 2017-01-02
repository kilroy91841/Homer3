USE homer;

ALTER TABLE draft_dollars
ADD COLUMN expired TINYINT(1) AFTER septemberStandingId;
ALTER TABLE history_draft_dollars
ADD COLUMN expired TINYINT(1) AFTER septemberStandingId;

UPDATE draft_dollars
SET expired = 1
WHERE season = 2016;
UPDATE draft_dollars
SET expired = 0
WHERE season <> 2016;
UPDATE history_draft_dollars
SET expired = 1
WHERE season = 2016;
UPDATE history_draft_dollars
SET expired = 0
WHERE season <> 2016;

ALTER TABLE draft_dollars
MODIFY COLUMN expired TINYINT(1) NOT NULL;
ALTER TABLE history_draft_dollars
MODIFY COLUMN expired TINYINT(1) NOT NULL;