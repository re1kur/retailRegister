create table enterprises
(
    id         integer default nextval('enterprises_entp_id_seq'::regclass) not null,
    name       varchar(30)                                                  not null,
    address    varchar(120),
    type       varchar(30)                                                  not null,
    password   varchar(64)                                                  not null,
    corp_email varchar(256)                                                 not null,
    constraint enterprise_pkey
        primary key (id),
    constraint enterprise_entp_address_key
        unique (address),
    unique (corp_email)
);

alter table enterprises
    owner to postgres;

create table employees
(
    entp_id   serial,
    firstname varchar(30)  not null,
    lastname  varchar(30)  not null,
    position  varchar(30)  not null,
    email     varchar(256) not null,
    password  varchar(64)  not null,
    rights    varchar(30),
    unique (email),
    foreign key (entp_id) references enterprises
);

alter table employees
    owner to postgres;

create table categories
(
    id      serial,
    entp_id integer     not null,
    name    varchar(40) not null,
    primary key (id),
    foreign key (entp_id) references enterprises
);

alter table categories
    owner to postgres;

create table units_measure
(
    id      serial,
    name    varchar(30) not null,
    entp_id integer     not null,
    symbol  varchar(5)  not null,
    primary key (id),
    unique (name),
    unique (symbol),
    foreign key (entp_id) references enterprises
);

alter table units_measure
    owner to postgres;

create table goods
(
    entp_id         integer                                                      not null,
    goods_name      varchar(40)                                                  not null,
    number          integer                                                      not null,
    goods_id        integer default nextval('products_product_id_seq'::regclass) not null,
    price           integer default 0                                            not null,
    category        integer,
    measure_unit_id integer,
    constraint products_pkey
        primary key (goods_id),
    constraint products_entp_id_fkey
        foreign key (entp_id) references enterprises,
    foreign key (category) references categories,
    foreign key (measure_unit_id) references units_measure
);

alter table goods
    owner to postgres;

create table support_reports
(
    id         serial,
    subject    varchar(255)                        not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    primary key (id)
);

alter table support_reports
    owner to postgres;


