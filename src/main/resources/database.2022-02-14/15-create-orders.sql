--liquibase formatted sql
--changeset kkorzeniewski:15
CREATE TABLE orders(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(45) NOT NULL,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    order_status ENUM('AWAITING_PAYMENT',
        'IN_PROGRESS',
        'SHIPPED',
        'DELIVERED',
        'CANCELED') NOT NULL,
    postal_code VARCHAR(6) NOT NULL,
    cart_id BIGINT DEFAULT NULL,
    user_id BIGINT NOT NULL,
    KEY FK594fgx8wpklcf3t41jq3grhlh (cart_id),
    KEY FK32ql8ubntj5uh44ph9659tiih (user_id),
    CONSTRAINT FK32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK594fgx8wpklcf3t41jq3grhlh FOREIGN KEY (cart_id) REFERENCES carts (id)
);