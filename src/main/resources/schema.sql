create table authorities (
    email varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(email) references user(email)
);
create unique index ix_auth_email on authorities (email,authority);