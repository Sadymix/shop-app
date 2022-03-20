--liquibase formatted sql
--changeset kkorzeniewski:3
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL
);