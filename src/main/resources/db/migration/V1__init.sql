CREATE TABLE products
(
	id		bigserial,	-- flyway не знает слово IDENTITY (наверное, не поддерживает h2)
	title	VARCHAR(255),
	price	INT,
	PRIMARY KEY (id)
);
INSERT INTO products (title, price) VALUES
	('Товар№01',  10.0),
	('Товар№02',  20.0),
	('Товар№03',  30.0),
	('Товар№04',  40.0),
	('Товар№05',  50.0),
	('Товар№06',  60.0),
	('Товар№07',  70.0),
	('Товар№08',  80.0),
	('Товар№09',  90.0),
	('Товар№10', 100.0),
	('Товар№11', 110.0),
	('Товар№12', 120.0),
	('Товар№13', 130.0),
	('Товар№14', 140.0),
	('Товар№15', 150.0),
	('Товар№16', 160.0),
	('Товар№17', 170.0),
	('Товар№18', 180.0),
	('Товар№19', 190.0),
	('Товар№20', 200.0);

CREATE TABLE ourusers
(
	id			bigserial,
	login		VARCHAR(32) NOT NULL UNIQUE,
	password	VARCHAR(64) NOT NULL,	-- размер 64 — для хэша (он, похоже, всегда занимает 60 символов, даже для пароля в 128 символов)
	email		VARCHAR(64) NOT NULL UNIQUE,
	created_at	TIMESTAMP DEFAULT current_timestamp,
	updated_at	TIMESTAMP DEFAULT current_timestamp,
	PRIMARY KEY (id)
);
INSERT INTO ourusers (login, password, email) VALUES
	('super',	'$2a$12$c4HYjryn7vo1bYQfSzkUDe8jPhYIpInbUKZmv5lGnmcyrQPLIWnVu',	'super@post.ru'),	-- пароль 100
	('admin',	'$2a$12$c4HYjryn7vo1bYQfSzkUDe8jPhYIpInbUKZmv5lGnmcyrQPLIWnVu',	'admin@post.ru'),	-- пароль 100
	('user1',	'$2a$12$c4HYjryn7vo1bYQfSzkUDe8jPhYIpInbUKZmv5lGnmcyrQPLIWnVu',	'user1@post.ru'),	-- пароль 100
	('user2',	'$2a$12$c4HYjryn7vo1bYQfSzkUDe8jPhYIpInbUKZmv5lGnmcyrQPLIWnVu',	'user2@post.ru');	-- пароль 100

CREATE TABLE roles
(
	id			serial,
	name		VARCHAR(64) NOT NULL UNIQUE,
	created_at	TIMESTAMP DEFAULT current_timestamp,
	updated_at	TIMESTAMP DEFAULT current_timestamp,
	PRIMARY KEY (id)
);
INSERT INTO roles (name) VALUES
	('ROLE_SUPERADMIN'), -- только для суперадминов
	('ROLE_ADMIN'),		 -- только для админов и суперадминов
	('ROLE_USER');		 -- только для авторизованных юзеров

CREATE TABLE ourusers_roles
(
	user_id		bigint NOT NULL,
	role_id		int NOT NULL,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY (user_id) REFERENCES ourusers (id),
	FOREIGN KEY (role_id) REFERENCES roles (id)
);
INSERT INTO ourusers_roles (user_id, role_id) VALUES
	(1, 1), -- super	ROLE_SUPERADMIN
	(2, 2), -- admin	ROLE_ADMIN
	(3, 3),	-- user1	ROLE_USER
	(4, 3); -- user2	ROLE_USER
