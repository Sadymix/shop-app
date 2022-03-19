--liquibase formatted sql
--changeset kkorzeniewski:5
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    KEY FKa62j07k5mhgifpp955h37ponj (roles_id),
    KEY FK2o0jvgh89lemvvo17cbqvdxaa (user_id),
    CONSTRAINT FK2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FKa62j07k5mhgifpp955h37ponj FOREIGN KEY (roles_id) REFERENCES roles (id)
);