DROP database if EXISTS eatme;
CREATE database eatme;
use eatme;

CREATE TABLE `users` (
	`user_id` INT(5) NOT NULL AUTO_INCREMENT, 
	`username` VARCHAR(50), 
	`password` VARCHAR(50),
	PRIMARY KEY(`user_id`), 
	INDEX(`username`)
	);

INSERT INTO users (username, password) VALUE ('milan', MD5('milan'));
INSERT INTO users (username, password) VALUE ('shamil', MD5('shamil'));
INSERT INTO users (username, password) VALUE ('ibrahima', MD5('ibrahima'));
INSERT INTO users (username, password) VALUE ('leo', MD5('leo'));