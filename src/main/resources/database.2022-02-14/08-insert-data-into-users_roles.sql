--liquibase formatted sql
--changeset kkorzeniewski:8
INSERT INTO users_roles(user_id, roles_id)
VALUES
    (1,1),
    (2,2),
    (3,3);