create table if not exists users
(
    id       bigint auto_increment not null,
    username varchar(30) unique    not null,
    password varchar(128)          not null,
    enabled  boolean               not null default true,
    primary key (id)
);

create table if not exists roles
(
    id   bigint auto_increment not null,
    name varchar(128)          not null,
    primary key (id)
);

create table if not exists users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);

INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name)
SELECT 'ROLE_USER'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
SELECT 'ROLE_REDACTOR'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_REDACTOR');

INSERT INTO users (username, password)
SELECT 'admin', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'admin'
  AND r.name IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_REDACTOR')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);

INSERT INTO users (username, password)
SELECT 'user', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'user'
  AND r.name IN ('ROLE_USER')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);