DROP TABLE IF EXISTS HISTORY;

CREATE TABLE HISTORY (
  ID varchar(36) NOT NULL PRIMARY KEY,
  PATH VARCHAR(100) NOT NULL,
  RESULT VARCHAR(200) NOT NULL
);

INSERT INTO HISTORY (ID, PATH, RESULT) VALUES ('1', 'PATH_1', 'RESULT_1');
INSERT INTO HISTORY (ID, PATH, RESULT) VALUES ('2', 'PATH_2', 'RESULT_2');
INSERT INTO HISTORY (ID, PATH, RESULT) VALUES ('3', 'PATH_3', 'RESULT_3');
INSERT INTO HISTORY (ID, PATH, RESULT) VALUES ('4', 'PATH_4', 'RESULT_4');
INSERT INTO HISTORY (ID, PATH, RESULT) VALUES ('5', 'PATH_5', 'RESULT_5');