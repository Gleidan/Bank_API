DROP TABLE IF EXISTS CARD CASCADE;
DROP TABLE IF EXISTS ACCOUNT CASCADE;
DROP TABLE IF EXISTS CLIENT CASCADE;

CREATE TABLE IF NOT EXISTS CLIENT
(
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME TEXT,
    SURNAME TEXT
);

CREATE TABLE IF NOT EXISTS ACCOUNT
(
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NUMBER UUID UNIQUE,
    CLIENT_ID BIGINT,
    FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CARD
(
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NUMBER UUID UNIQUE,
    ACCOUNT_ID BIGINT,
    BALANCE DOUBLE,
    FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ID) ON DELETE CASCADE
);