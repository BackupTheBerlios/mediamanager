-- MediaManager database, core tables --
--
-- SQL statements for Mckoi database, see http://mckoi.com/database/
-- 
-- author: crac 
-- $Id: mm_core.sql,v 1.2 2004/06/25 08:57:31 crac Exp $

CREATE TABLE Ent (
  EntId INTEGER DEFAULT UNIQUEKEY('Ent') NOT NULL,
  EntName VARCHAR(255) DEFAULT '' NOT NULL UNIQUE,
  PRIMARY KEY (EntId),
  UNIQUE (EntName)
);

CREATE TABLE Fldtype (
  FldtypeId INTEGER DEFAULT UNIQUEKEY('Fldtype') NOT NULL,
  FldtypeValue VARCHAR(50) DEFAULT '' NOT NULL,
  PRIMARY KEY (FldtypeId)
);

INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (1, 'PK');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (2, 'EntryId');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (3, 'UserId');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (4, 'int');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (5, 'varchar');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (6, 'text');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (7, 'boolean');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (8, 'List');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (9, 'Date');
INSERT INTO Fldtype (FldtypeId, FldtypeValue) VALUES (10, 'Timestamp');

CREATE TABLE Entry (
  EntryId INTEGER DEFAULT UNIQUEKEY('Entry') NOT NULL,
  EntryCreation TIMESTAMP NOT NULL,
  EntryEdit TIMESTAMP DEFAULT 0 NOT NULL,
  PRIMARY KEY (EntryId)
);

CREATE TABLE Fld (
  FldId INTEGER DEFAULT UNIQUEKEY('Fld') NOT NULL,
  FldName VARCHAR(255) DEFAULT '' NOT NULL,
  FldEntId INTEGER NOT NULL,
  FldFldtypeId INTEGER NOT NULL,
  FldLength INTEGER DEFAULT 0 NOT NULL,
  FldDefault VARCHAR(255) DEFAULT '' NOT NULL,
  FldHidden TINYINT DEFAULT 0 NOT NULL,
  FldMandatory TINYINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (FldId),
  FOREIGN KEY (FldEntId) REFERENCES Ent (EntId),
  FOREIGN KEY (FldFldtypeId) REFERENCES Fldtype (FldtypeId)
);