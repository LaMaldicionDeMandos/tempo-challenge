DROP TABLE IF EXISTS HISTORY;

CREATE TABLE HISTORY (
  ID varchar(36) NOT NULL PRIMARY KEY,
  PATH VARCHAR(100) NOT NULL,
  RESULT VARCHAR(200) NOT NULL
);