DROP TABLE IF EXISTS coffee_houses CASCADE;
CREATE TABLE coffee_houses (
    address VARCHAR(255) PRIMARY KEY,
    space DOUBLE precision NOT NULL,
    rental_price DOUBLE precision NOT NULL,
    mobile_number VARCHAR(13) NOT NULL
);

DROP TABLE IF EXISTS employees CASCADE;
CREATE TABLE employees (
    full_name VARCHAR(255) NOT NULL PRIMARY KEY,
    birthday_date BIGINT NOT NULL,
    mobile_number VARCHAR(13) NOT NULL,
    address VARCHAR(255) NOT NULL,
    passport VARCHAR(10) NOT NULL,
    sex VARCHAR(10) NOT NULL
);

DROP TABLE IF EXISTS positions CASCADE;
CREATE TABLE positions (
    name VARCHAR(255) NOT NULL PRIMARY KEY,
    avg_salary DOUBLE precision NOT NULL,
    priority INTEGER NOT NULL,
    description VARCHAR(255)
);

DROP TABLE IF EXISTS contracts CASCADE;
CREATE TABLE contracts (
    contract_number INTEGER NOT NULL PRIMARY KEY,
    position VARCHAR(255) NOT NULL REFERENCES positions(name) ON DELETE RESTRICT,
    start_date BIGINT NOT NULL,
    end_date BIGINT,
    hours_per_week INTEGER NOT NULL default 40,
    employee VARCHAR(255) NOT NULL REFERENCES employees(full_name) ON DELETE RESTRICT,
    coffee_house VARCHAR(255) NOT NULL REFERENCES coffee_houses(address) ON DELETE RESTRICT,
    salary DOUBLE precision NOT NULL default 3200,
    vacation INTEGER
);

DROP TABLE IF EXISTS coffee_drinks CASCADE;
CREATE TABLE coffee_drinks (
    name VARCHAR(255) NOT NULL PRIMARY KEY,
    price DOUBLE precision NOT NULL,
    native_price DOUBLE precision NOT NULL,
    description VARCHAR(255)
);

DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products (
    name VARCHAR(255) NOT NULL PRIMARY KEY,
    price DOUBLE precision NOT NULL,
    unit_of_measurement VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

DROP TABLE IF EXISTS suppliers CASCADE;
CREATE TABLE suppliers (
    full_name VARCHAR(255) NOT NULL PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    product VARCHAR(255) NOT NULL REFERENCES products(name) ON DELETE RESTRICT,
    coffee_house VARCHAR(255) NOT NULL REFERENCES coffee_houses(address) ON DELETE RESTRICT,
    mobile_number VARCHAR(13) NOT NULL,
    manager VARCHAR(255)
);

DROP TABLE IF EXISTS sales_reports CASCADE;
CREATE TABLE sales_reports (
    coffee_drink VARCHAR(255) NOT NULL REFERENCES coffee_drinks(name) ON DELETE RESTRICT,
    employee VARCHAR(255) NOT NULL REFERENCES employees(full_name) ON DELETE RESTRICT,
    price_with_vat DOUBLE precision NOT NULL,
    sale_date BIGINT NOT NULL,
    coffee_house VARCHAR(255) NOT NULL REFERENCES coffee_houses(address) ON DELETE RESTRICT,
    PRIMARY KEY (coffee_drink, employee, sale_date)
);