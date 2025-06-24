--liquibase formatted sql

--changeset marco:4_add_price_to_games
ALTER TABLE Games ADD COLUMN preco DOUBLE;