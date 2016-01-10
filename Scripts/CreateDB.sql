drop database if exists dbusers;
create database dbusers;
use dbusers;

CREATE TABLE `users` (
	`user_id` INT(5) NOT NULL AUTO_INCREMENT, 
	`username` VARCHAR(50), 
	`password` VARCHAR(50) ,
	PRIMARY KEY(`user_id`), 
	INDEX(`username`)
	);
	
	
 