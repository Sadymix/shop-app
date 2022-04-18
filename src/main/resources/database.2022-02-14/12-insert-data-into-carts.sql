--liquibase formatted sql
--changeset kkorzeniewski:12
INSERT INTO carts(total_price, user_id)
    VALUES (10.98, 1),
           (199.99, 2),
           (1.98, 3);
