create sequence brand_seq start with 1 increment by 1;
create sequence bucket_seq start with 1 increment by 1;
create sequence categories_seq start with 1 increment by 1;
create sequence order_details_seq start with 1 increment by 1;
create sequence order_seq start with 1 increment by 1;
create sequence payment_seq start with 1 increment by 1;
create sequence product_seq start with 1 increment by 1;
create sequence review_seq start with 1 increment by 1;
create sequence shipping_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;
create table brands
(
    id          bigint       not null,
    name        varchar(50)  not null,
    description varchar(500) not null,
    primary key (id)
);
create table buckets
(
    id      bigint not null,
    user_id bigint unique,
    primary key (id)
);
create table buckets_products
(
    bucket_id  bigint not null,
    product_id bigint not null
);
create table categories
(
    id    bigint not null,
    title varchar(255),
    primary key (id)
);
create table orders
(
    sum     numeric(38, 2),
    created timestamp(6),
    id      bigint not null,
    updated timestamp(6),
    user_id bigint,
    address varchar(255),
    status  varchar(255) check (status in ('NEW', 'APPROVED', 'CANCELED', 'PAID', 'CLOSED')),
    primary key (id)
);
create table orders_details
(
    amount     numeric(38, 2),
    price      numeric(38, 2),
    id         bigint not null,
    order_id   bigint,
    product_id bigint,
    primary key (id)
);
create table payments
(
    amount       numeric(38, 2) not null,
    date         timestamp(6)   not null,
    id           bigint         not null,
    product_id   bigint unique,
    payment_type varchar(255)   not null check (payment_type in ('CASH', 'BY_CARD')),
    primary key (id)
);
create table product_tags
(
    product_id bigint not null,
    tag_name   varchar(255)
);
create table products
(
    availability      boolean,
    price             numeric(38, 2) not null,
    quantity          integer        not null,
    sale              numeric(38, 2),
    weight            float4,
    brand_id          bigint,
    created_at        timestamp(6),
    id                bigint         not null,
    updated_at        timestamp(6),
    title             varchar(100)   not null,
    care_instructions varchar(500),
    description       varchar(500)   not null,
    color             varchar(255),
    material          varchar(255),
    size              varchar(255) check (size in ('S','M','L','XL','XXL')
) , primary key (id));
create table products_categories
(
    category_id bigint not null,
    product_id  bigint not null
);
create table reviews
(
    rating      integer      not null,
    created     timestamp(6),
    id          bigint       not null,
    product_id  bigint,
    reviewer_id bigint,
    updated     timestamp(6),
    comment     varchar(255) not null,
    primary key (id)
);
create table "shipping's"
(
    delivery_date    date,
    shipping_cost    numeric(38, 2),
    shipping_date    date,
    id               bigint not null,
    order_id         bigint unique,
    shipping_company varchar(255),
    shipping_type    varchar(255) check (shipping_type in ('PICKUP', 'COURIER', 'MAIL')),
    tracking_number  varchar(255),
    primary key (id)
);
create table users
(
    archive  boolean not null,
    id       bigint  not null,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    role     varchar(255) check (role in ('CLIENT', 'MANAGER', 'ADMIN')),
    primary key (id)
);
alter table if exists buckets add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references users;
alter table if exists buckets_products add constraint FKloyxdc1uy11tayedf3dpu9lci foreign key (product_id) references products;
alter table if exists buckets_products add constraint FKc49ah45o66gy2f2f4c3os3149 foreign key (bucket_id) references buckets;
alter table if exists orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users;
alter table if exists orders_details add constraint FK5o977kj2vptwo70fu7w7so9fe foreign key (order_id) references orders;
alter table if exists orders_details add constraint FKs0r9x49croribb4j6tah648gt foreign key (product_id) references products;
alter table if exists payments add constraint FK7t03l8tlgddntpigs5oe1xyo6 foreign key (product_id) references products;
alter table if exists product_tags add constraint FK5rk6s19k3risy7q7wqdr41uss foreign key (product_id) references products;
alter table if exists products add constraint FKa3a4mpsfdf4d2y6r8ra3sc8mv foreign key (brand_id) references brands;
alter table if exists products_categories add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references categories;
alter table if exists products_categories add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references products;
alter table if exists reviews add constraint FKpl51cejpw4gy5swfar8br9ngi foreign key (product_id) references products;
alter table if exists reviews add constraint FKd1isgfajhtdl8mgg29up6mofi foreign key (reviewer_id) references users;
alter table if exists "shipping's" add constraint FK2wvo3ppsov8kf7aqva7imyrav foreign key (order_id) references orders;