INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO categories (name) VALUES ('food'), ('drink'), ('sport'), ('fun');

INSERT INTO users (username, password, firstname, lastname, email, enabled) VALUES
	('ananas', '{noop}ananas', 'Anatol', 'Mongoł', 'among@mail.com', 1);
	
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);