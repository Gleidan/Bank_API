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

INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');
INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Vasya','Vasyov');
INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);
INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9110', 1);
INSERT INTO card(id, number, account_id, balance) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 999.99);
INSERT INTO card(id, number, account_id, balance) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9011', 1, 315.21);
INSERT INTO card(id, number, account_id, balance) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9012', 1, 12312.32131);
INSERT INTO card(id, number, account_id, balance) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9021', 2, 315.21);
