--liquibase formatted sql
--changeset kkorzeniewski:14
UPDATE carts
SET total_price = 0.00
WHERE total_price IS NOT NULL;
