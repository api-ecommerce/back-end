CREATE DATABASE ecommerce;

USE ecommerce;

CREATE TABLE tb_user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(250) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    administrador BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO tb_user (nome, cpf, email, senha) VALUES ('João Silva', '12345678901', 'joao.silva@example.com', 'senha123');

select * from tb_user