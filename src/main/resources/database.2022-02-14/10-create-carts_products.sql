--liquibase formatted sql
--changeset kkorzeniewski:10
CREATE TABLE carts_products (
    cart_id BIGINT NOT NULL,
    products_id BIGINT NOT NULL,
    KEY FKq9ns7lphr8im6vg3i5dwknsbn (products_id),
    KEY FK3nvguygrfbn661omvvu2uafu5 (cart_id),
    CONSTRAINT FK3nvguygrfbn661omvvu2uafu5 FOREIGN KEY (cart_id) REFERENCES carts (id),
    CONSTRAINT FKq9ns7lphr8im6vg3i5dwknsbn FOREIGN KEY (products_id) REFERENCES products (id)
);