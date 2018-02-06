CREATE DATABASE blogtech with ENCODING = utf8 LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';

Create table if not exists users (
	oid char(24), -- objectId
	username varchar(32) not null UNIQUE, -- 8 utf8
	email varchar(50) not null UNIQUE,
	password varchar(50) not null,
	primary key (oid)
);
CREATE INDEX index_username ON users (user_name);
CREATE INDEX index_email ON users (email);
