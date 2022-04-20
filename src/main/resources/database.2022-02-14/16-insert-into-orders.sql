--liquibase formatted sql
--changeset kkorzeniewski:16
INSERT INTO orders(address, city, first_name, last_name, order_status, postal_code, cart_id, user_id)
    VALUES("Long 10", "New York", "John", "Ball", "AWAITING_PAYMENT", "11-111", 1, 1);