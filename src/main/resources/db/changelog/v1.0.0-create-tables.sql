--liquibase formatted sql

--changeset marco:2_create_users_table  -- Mudei o ID aqui
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255)
);

--changeset marco:3_create_games_table  -- E aqui
CREATE TABLE Games (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    description TEXT,
    platform VARCHAR(100)
);