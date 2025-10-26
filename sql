CREATE DATABASE webappdemo;
USE webappdemo;

CREATE TABLE users (
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(50)
);
INSERT INTO users VALUES ('admin', '1234');

CREATE TABLE employee (
  EmpID INT PRIMARY KEY,
  Name VARCHAR(50),
  Salary DOUBLE
);
INSERT INTO employee VALUES (1, 'Aatrey', 50000), (2, 'Sravarn', 60000);

CREATE TABLE attendance (
  StudentID INT,
  Date DATE,
  Status VARCHAR(10)
);
