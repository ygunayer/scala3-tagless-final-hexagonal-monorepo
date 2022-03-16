create table account(
    id bigserial primary key,
    created_at timestamp not null default now(),
    updated_at timestamp,
    status smallint not null default 0,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(255),
    is_master boolean not null default false,
    gsm_number varchar(50),
    password text not null,
    activation_token varchar(255),
    activation_token_expires_at timestamp
);
