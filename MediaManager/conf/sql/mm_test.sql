 -- Testing stuff --

CREATE TABLE Test (
  TestId INTEGER DEFAULT UNIQUEKEY('Test') NOT NULL,
  EntryId INTEGER NOT NULL,
  Interpret VARCHAR(255) DEFAULT '' NOT NULL,
  Stil VARCHAR(255) DEFAULT '' NOT NULL,
  PRIMARY KEY (TestId),
  FOREIGN KEY (EntryId) REFERENCES Entry (EntryId)
);

INSERT INTO Ent VALUES (1, 'Test');

INSERT INTO Fld VALUES (1, 'TestId', 1, 1, 0, '', 1, 1);
INSERT INTO Fld VALUES (2, 'Interpret', 1, 5, 255, '', 0, 1);
INSERT INTO Fld VALUES (3, 'Stil', 1, 5, 255, '', 0, 1);

INSERT INTO Users VALUES ('Tom', 'Test', 'A');
INSERT INTO Entry VALUES (1, 'A', TONUMBER(DATEOB('2004-06-20')), 0);
INSERT INTO Test VALUES (1, 1, 'Krass', 'Rock');