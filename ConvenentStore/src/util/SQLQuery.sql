IF EXISTS (SELECT name FROM sys.databases WHERE name = 'ConvinentStore')
DROP DATABASE ConvinentStore;
GO

CREATE DATABASE ConvinentStore;
GO

--USE ConvenentStore;
USE ConvinentStore;
GO

CREATE TABLE account (
    id INT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) CHECK (role IN ('admin', 'employee')) NOT NULL
);

INSERT INTO account (id, username, password, role) VALUES ('1', 'admin', 'admin123', 'admin');
INSERT INTO account (id, username, password, role) VALUES ('2', 'emp', 'emp123', 'employee');
GO

CREATE TABLE product (
product_id int PRIMARY KEY,
type varchar(100),
brand varchar(100),
productName varchar(100),
price float,
status varchar(100),
image varchar(500),
[date] Date
);
GO

CREATE TABLE order_detail (
id int PRIMARY KEY NOT NULL IDENTITY(1,1),
order_id int NOT NULL,
product_id int NOT NULL,
productName varchar(100) NOT NULL,
type varchar(100),
brand varchar(100),
quantity int NOT NULL,
price float NOT NULL,
[date] Date,
FOREIGN KEY (product_id) REFERENCES product(product_id)
);


CREATE TABLE orders (
order_id int PRIMARY KEY NOT NULL,
total float NOT NULL,
amount float,
balance float,
acc_id int,
[date] Date,
FOREIGN KEY (acc_id) REFERENCES account(id)
);
GO