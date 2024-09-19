create table tb_product(

product_id varchar(250) PRIMARY KEY,
category_id varchar(250),
name varchar(250) not null,
description text not null,
price_in_cents int not null,
stock_quantity int not null,
sku varchar(250) not null unique,
image blob not null,
created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (category_id) REFERENCES tb_category(category_id)
);