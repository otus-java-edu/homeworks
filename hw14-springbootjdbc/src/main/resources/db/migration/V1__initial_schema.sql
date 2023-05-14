create table client
(
    id   serial not null primary key,
    name varchar(50)
);

create table address
(
    client_id integer not null references client (id),
    address   varchar(50)
);

create table phone
(
    id        serial not null primary key,
    phone     varchar(50),
    client_id integer not null references client (id)
);