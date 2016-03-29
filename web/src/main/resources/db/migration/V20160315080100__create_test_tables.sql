USE homer;

DROP TABLE IF EXISTS test;
CREATE TABLE test (
	id BIGINT NOT NULL AUTO_INCREMENT,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    count INT NOT NULL,
    name VARCHAR(100),
    longObject BIGINT NULL,
    myBool TINYINT(1) NOT NULL DEFAULT 0,
    dummyEnum INT NOT NULl,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS history_test;
CREATE TABLE history_test (
    historyId BIGINT NOT NULL AUTO_INCREMENT,
	id BIGINT NOT NULL,
    createdDateUTC DATETIME NOT NULL,
    updatedDateUTC DATETIME NOT NULL,
    count INT NOT NULL,
    name VARCHAR(100),
    longObject BIGINT NULL,
    myBool TINYINT(1) NOT NULL DEFAULT 0,
    dummyEnum INT NOT NULl,
    historyCreatedDateUTC DATETIME NOT NULL,
    isDeleted TINYINT(1),
    PRIMARY KEY (historyId)
);