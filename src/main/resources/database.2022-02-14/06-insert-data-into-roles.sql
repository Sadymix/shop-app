--liquibase formatted sql
--changeset kkorzeniewski:6
INSERT INTO roles(name)
VALUES
    ("USER"),
    ("ADMIN"),
    ("STUFF");