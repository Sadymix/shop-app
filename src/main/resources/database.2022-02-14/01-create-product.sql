--liquibase formatted sql
--changeset kkorzeniewski:1
CREATE TABLE products(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    color VARCHAR(45) NOT NULL,
    type ENUM('CLOTHES', 'ACCESSORIES', 'SHOES', 'SUPPLEMENTS', 'FOOD') NOT NULL
);