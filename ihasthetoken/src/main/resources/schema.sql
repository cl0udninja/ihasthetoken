create database ihasthetoken; create user ihasthetoken with encrypted password 'ihasthetoken'; grant all privileges on database ihasthetoken to ihasthetoken;
	
	create table if not exists token_users (
		id	varchar(255) CONSTRAINT pk_user PRIMARY KEY not null UNIQUE,
		name	varchar(255) not null UNIQUE,
		email	varchar(255) not null UNIQUE
	);
	
	create table if not exists token_queue (
		id	varchar(255) CONSTRAINT pk_token PRIMARY KEY not null UNIQUE,
		user_id	varchar(255) references token_users (id) not null,
		created	timestamp with time zone not null,
		status varchar(15) not null
	);
