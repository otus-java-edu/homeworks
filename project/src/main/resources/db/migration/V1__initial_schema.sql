create table trades
(
    id                  bigint,
    user_id             integer,
    symbol_id           integer,
    direction           smallint,
    price               real,
    volume              real,
    commission          real,
    commission_asset    varchar(5),
    time                bigint,
    maker               boolean
);
alter table trades add primary key (id, user_id, symbol_id);

create table users
(
    id                  serial not null primary key,
    name                varchar(50),
    api_key             varchar(100),
    secret_key          varchar(100),
    initialized         boolean
);
create table symbols
(
    id                  serial not null primary key,
    name                varchar(10),
    CONSTRAINT name_unique UNIQUE (name)
);

create table user_data
(
    user_id             integer,
    symbol_id           integer,
    last_trade          bigint,
    last_sync_time      bigint,
    last_pnl_time       bigint
);

alter table user_data add primary key (user_id, symbol_id);

create table bnb_data
(
    time                bigint not null primary key,
    price               real
);

create table pnl
(
    user_id             integer,
    symbol_id           integer,
    time                bigint,
    profit              real,
    volume              real,
    entry_price         real,
    commissions         real
);

alter table pnl add primary key (user_id, symbol_id, time);

insert into symbols (name) values ('bnbbusd'), ('bnbusdt'), ('adabusd'), ('adausdt'), ('btcbusd'), ('btcusdt'), ('ethbusd'), ('ethusdt'),
('etcbusd'), ('etcusdt'), ('dogebusd'), ('dogeusdt'), ('ltcbusd'), ('ltcusdt'), ('xrpbusd'), ('xrpusdt'),
('filbusd'), ('filusdt'), ('egldusdt'), ('maticbusd'), ('maticusdt'), ('trxbusd'), ('trxusdt'),
('ftmbusd'), ('ftmusdt'), ('galabusd'), ('galausdt'), ('avaxbusd'), ('avaxusdt');

