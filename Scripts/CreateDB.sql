DROP database if EXISTS dbusers;
CREATE database dbusers;
use dbusers;

CREATE TABLE 'Users' (
	'user_id' INT(5) NOT NULL AUTO_INCREMENT, 
	'username' VARCHAR(50), 
	'password' VARCHAR(50) ,
	PRIMARY KEY(`user_id`), 
	INDEX(`username`)
	);