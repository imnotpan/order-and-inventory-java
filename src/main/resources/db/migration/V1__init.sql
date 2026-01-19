create table products (
    id          bigserial primary key,
    sku         varchar(64) not null unique,
    name        varchar(255) not null,
    price       numeric(12,2) not null check (price >= 0),
    active      boolean not null default true,
    created_at  timestamp not null default now(),
    updated_at  timestamp not null default now()
);

create table inventory (
    product_id          bigint primary key,
    available_quantity  bigint not null check (available_quantity >= 0),
    reserved_quantity   bigint not null check (reserved_quantity >= 0),
    version             bigint not null,
    constraint fk_inventory_product
        foreign key (product_id)
        references products(id)
        on delete cascade
);

create table customers (
    id          bigserial primary key,
    name        varchar(255) not null,
    email       varchar(255) not null unique,
    created_at  timestamp not null default now()
);

create table orders (
    id              bigserial primary key,
    customer_id     bigint not null,
    status          varchar(32) not null,
    total_amount    numeric(14,2) not null check (total_amount >= 0),
    created_at      timestamp not null default now(),
    updated_at      timestamp not null default now(),
    constraint fk_orders_customer
        foreign key (customer_id)
        references customers(id)
);

create table order_lines (
    id              bigserial primary key,
    order_id        bigint not null,
    product_id      bigint not null,
    quantity        bigint not null check (quantity > 0),
    unit_price      numeric(12,2) not null check (unit_price >= 0),
    constraint fk_order_lines_order
        foreign key (order_id)
        references orders(id)
        on delete cascade,
    constraint fk_order_lines_product
        foreign key (product_id)
        references products(id)
);

-- INDEXES
create index idx_products_sku on products(sku);
create index idx_orders_customer on orders(customer_id);
create index idx_orders_status on orders(status);
create index idx_order_lines_order on order_lines(order_id);
