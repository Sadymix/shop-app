--liquibase formatted sql
--changeset kkorzeniewski:2
INSERT INTO products(name, price, color, type)
VALUE("shirt", 59.99, "black", "CLOTHES");