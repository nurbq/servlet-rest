create table users
(
    id      serial primary key,
    name    text    not null,
    surname text    not null,
    age     integer not null
);

create table users_activities
(
    id          serial primary key,
    user_id     integer references users (id),
    description text not null,
    date_time   timestamp(0) default now()
);