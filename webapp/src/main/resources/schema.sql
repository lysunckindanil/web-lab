create table if not exists users
(
    id       bigint auto_increment not null,
    username varchar(30) unique    not null,
    password varchar(128)          not null,
    enabled  boolean               not null,
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