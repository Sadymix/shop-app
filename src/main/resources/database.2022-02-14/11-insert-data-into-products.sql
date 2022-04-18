--liquibase formatted sql
--changeset kkorzeniewski:11
INSERT INTO products(name, price, color, type)
    VALUES ("Energy Bar", "0.99", "none", "FOOD"),
           ("Running Top", "19.99", "white", "CLOTHES"),
           ("Rubber band", "9.99", "red", "ACCESSORIES"),
           ("Protein", "39.99", "none", "SUPPLEMENTS"),
           ("Running shoes", "199.99", "green", "SHOES");