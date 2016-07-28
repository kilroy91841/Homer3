USE homer;

DROP TABLE IF EXISTS ref_transaction_type;
CREATE TABLE IF NOT EXISTS ref_transaction_type (
    id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO ref_transaction_type (id, name)
VALUES
(1, 'Add'),
(2, 'Drop'),
(3, 'Move'),
(4, 'Trade'),
(5, 'Unknown');

DROP TABLE IF EXISTS transactions;
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    playerId BIGINT NOT NULL,
    teamId BIGINT NOT NULL,
    transactionType INT NOT NULL,
    transactionDate DATETIME NOT NULL,
    text VARCHAR(100) NOT NULL,
    oldPosition INT,
    newPosition INT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerId) REFERENCES players (id),
    FOREIGN KEY (teamId) REFERENCES teams (id),
    FOREIGN KEY (transactionType) REFERENCES ref_transaction_type (id)
);

ALTER TABLE transactions AUTO_INCREMENT = 1;