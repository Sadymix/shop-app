--liquibase formatted sql
--changeset kkorzeniewski:7
INSERT INTO users( username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled)
VALUES
    ("user", "$2a$10$Pruy1HiYlsqZcJR3wuU/XeZWXiEEf.c33Zi346mcQouDM30E0uDxK", true, true, true, true),
    ("admin", "$2a$10$LL9u3c0eMLOr3zqMoVE31eE4ekGS9p9DJ4Zw/7nceSmQQ5WeGK8pm", true, true, true, true),
    ("staff", "$2a$10$zs6qTECzSPHk700GBddY1eveWl3CmIdQGhvrAbKcbSe/cQ00bN4OG", true, true, true, true);
