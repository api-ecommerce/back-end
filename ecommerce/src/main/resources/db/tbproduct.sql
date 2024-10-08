CREATE TABLE tb_product (
    product_id VARCHAR(250) PRIMARY KEY,
    category_id VARCHAR(250),
    name VARCHAR(250) NOT NULL,
    description TEXT NOT NULL,
    price_in_cents INT NOT NULL,
    stock_quantity INT NOT NULL,
    sku VARCHAR(250) NOT NULL UNIQUE,
    image BLOB NOT NULL,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(250),
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id),
    FOREIGN KEY (created_by) REFERENCES tb_user(user_id)
);
