--liquibase formatted sql
--changeset kkorzeniewski:4
CREATE TABLE users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accountNonExpired BOOLEAN NOT NULL,
    accountNonLocked BOOLEAN NOT NULL,
    credentialsNonExpired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    password VARCHAR(60) NOT NULL,
    username VARCHAR(45) NOT NULL
);