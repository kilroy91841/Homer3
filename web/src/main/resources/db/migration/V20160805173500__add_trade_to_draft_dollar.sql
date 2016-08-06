USE homer;

ALTER TABLE draft_dollars
ADD COLUMN tradeId BIGINT AFTER amount;

ALTER TABLE history_draft_dollars
ADD COLUMN tradeId BIGINT AFTER amount;