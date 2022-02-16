--liquibase formatted sql
--changeset kkorzeniewski:create-products-table
CREATE TABLE products(
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    price DECIMAL(9,2) NOT NULL,
    color VARCHAR(45) NOT NULL,
    type ENUM('CLOTHES', 'ACCESSORIES', 'SHOES', 'SUPPLEMENTS', 'FOOD') NOT NULL
);