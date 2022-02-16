--liquibase formatted sql
--changeset kkorzeniewski:insert-products-into-products-table
insert into products(id, name, price, color, type) values (UUID_TO_BIN(UUID()), 'shirt', 59.99, 'black', 'CLOTHES');