INSERT INTO CLIENT(ID, NAME, SURNAME) VALUES(DEFAULT,'VLAD','Savostikov');
INSERT INTO CLIENT(ID, NAME, SURNAME) VALUES(DEFAULT,'Ivan','Ivanov');
INSERT INTO ACCOUNT(ID, NUMBER, CLIENT_ID) VALUES (DEFAULT, '43da9b65-2184-469c-b9e4-f7d3cfbf9002', 1);
INSERT INTO ACCOUNT(ID, NUMBER, CLIENT_ID) VALUES (DEFAULT, '43da9b65-2184-469c-b9e4-f7d3cfbf9003', 2);
INSERT INTO CARD(ID, NUMBER, ACCOUNT_ID, BALANCE) VALUES (DEFAULT, '43da9b65-2184-469c-b9e4-f7d3cfbf9004', 1, '540.210');
INSERT INTO CARD(ID, NUMBER, ACCOUNT_ID, BALANCE) VALUES (DEFAULT, '43da9b65-2184-469c-b9e4-f7d3cfbf9005', 2, '210.2' );