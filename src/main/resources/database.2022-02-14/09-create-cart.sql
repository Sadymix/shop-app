--liquibase formatted sql
--changeset kkorzeniewski:9
CREATE TABLE carts(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price decimal(19,2) DEFAULT 0,
    user_id BIGINT NOT NULL,
    KEY FKb5o626f86h46m4s7ms6ginnop (user_id),
    CONSTRAINT FKb5o626f86h46m4s7ms6ginnop FOREIGN KEY (user_id) REFERENCES users (id)
);