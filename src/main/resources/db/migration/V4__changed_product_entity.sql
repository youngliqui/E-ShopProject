alter table if exists products add column sale numeric (38,2)
alter table if exists products add column updated_at timestamp (6)
alter table if exists products add column weight float4
alter table if exists products add column brand_id bigint
create table reviews
(
    id          bigint       not null,
    comment     varchar(255) not null,
    created     timestamp(6),
    rating      integer      not null,
    updated     timestamp(6),
    product_id  bigint,
    reviewer_id bigint,
    primary key (id)
)
create table "shipping's"
(
    id               bigint not null,
    delivery_date    date,
    shipping_company varchar(255),
    shipping_cost    numeric(38, 2),
    shipping_date    date,
    shipping_type    varchar(255) check (shipping_type in ('PICKUP', 'COURIER', 'MAIL')),
    tracking_number  varchar(255),
    order_id         bigint,
    primary key (id)
)
alter table if exists payments drop constraint if exists UK_nqhfuh70ud9tavdnm76pxdtn6
alter table if exists "shipping's" add constraint UK_9dttifv7rfx3twqtvxk8p7h21 unique (order_id)
create sequence brand_seq start with 1 increment by 1
create sequence payment_seq start with 1 increment by 1
create sequence review_seq start with 1 increment by 1
create sequence shipping_seq start with 1 increment by 1
alter table if exists payments add constraint FK7t03l8tlgddntpigs5oe1xyo6 foreign key (product_id) references products
alter table if exists product_tags add constraint FK5rk6s19k3risy7q7wqdr41uss foreign key (product_id) references products
alter table if exists products add constraint FKa3a4mpsfdf4d2y6r8ra3sc8mv foreign key (brand_id) references brands
alter table if exists reviews add constraint FKpl51cejpw4gy5swfar8br9ngi foreign key (product_id) references products
alter table if exists reviews add constraint FKd1isgfajhtdl8mgg29up6mofi foreign key (reviewer_id) references users
alter table if exists "shipping's" add constraint FK2wvo3ppsov8kf7aqva7imyrav foreign key (order_id) references orders