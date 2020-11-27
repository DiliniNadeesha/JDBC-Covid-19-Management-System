create database covid19DB;

use covid19DB;

CREATE TABLE GlobalCovidData (
  updatedDate DATE NOT NULL,
  confirmed VARCHAR(25) DEFAULT NULL,
  recovered VARCHAR(25) DEFAULT NULL,
  death VARCHAR(25) DEFAULT NULL,
  PRIMARY KEY (updatedDate)
);


CREATE TABLE Hospital (
  hospitalID VARCHAR(10) NOT NULL,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL,
  district VARCHAR(50) NOT NULL,
  capacity VARCHAR(50) NOT NULL,
  director VARCHAR(100) NOT NULL,
  directorContactNo VARCHAR(11) NOT NULL,
  hospitalContactNo1 VARCHAR(11) NOT NULL,
  hospitalContactNo2 VARCHAR(11) NOT NULL,
  faxNo varchar(15) NOT NULL,
  email varchar(100) NOT NULL,
  PRIMARY KEY (hospitalID)
);


CREATE TABLE QuarantineCenter (
  quarantineCenterID varchar(10) NOT NULL,
  name varchar(100) NOT NULL,
  city varchar(50) NOT NULL,
  district varchar(50) NOT NULL,
  head varchar(50) NOT NULL,
  headContactNo VARCHAR(11) NOT NULL,
  centerContactNo1 VARCHAR(11) NOT NULL,
  centerContactNo2 VARCHAR(11) NOT NULL,
  capacity varchar(50) NOT NULL,
  PRIMARY KEY (quarantineCenterID)
);


CREATE TABLE user (
  name varchar(100) NOT NULL,
  contact varchar(12) NOT NULL,
  email varchar(100) NOT NULL,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  userRole VARCHAR(50) NOT NULL,
  hospitalID VARCHAR(10) DEFAULT NULL,
  quarantineCenterID VARCHAR(10) DEFAULT NULL,
  PRIMARY KEY (name),
  UNIQUE KEY contact (contact),
  UNIQUE KEY email (email),
  UNIQUE KEY username (username),
  UNIQUE KEY password (password),
  CONSTRAINT FOREIGN KEY (hospitalID) REFERENCES Hospital(HospitalID) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (quarantineCenterID) REFERENCES QuarantineCenter(QuarantineCenterID) ON UPDATE CASCADE ON DELETE CASCADE
);


