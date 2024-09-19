create table tb_category (

category_id varchar(250) primary key,
name varchar(250) not null,
description text,
status boolean not null default true,
created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);