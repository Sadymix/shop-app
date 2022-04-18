--liquibase formatted sql
--changeset kkorzeniewski:13
INSERT INTO carts_products(cart_id, products_id)
    VALUES (1, 2),
           (1, 4),
           (2, 6),
           (3, 2),
           (3, 2);
