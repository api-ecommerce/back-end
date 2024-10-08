CREATE TABLE tb_category (
    category_id VARCHAR(250) PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    description TEXT,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(250),
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES tb_user(user_id)
);