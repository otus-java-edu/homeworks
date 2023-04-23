-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    address varchar(50)
);

create sequence phone_SEQ start with 1 increment by 1;

create table phone
(
    id   bigint not null primary key,
    phone varchar(50),
    client_id bigint not null
);