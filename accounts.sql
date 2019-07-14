drop database if exists paint;

create database paint;

use paint;

drop table if exists accounts;

-- ------------------accounts table--------------
create table accounts
(id integer auto_increment primary key not null,
 user_name varchar(255) unique not null,
 authentication_string text not null,
 register_date datetime not null default current_timestamp,
 avatar_id integer not null default 1
);

-- --------------------friend_connections--------------
create table friend_connections
(id integer auto_increment primary key not null,
user1_id integer not null,
user2_id integer not null,
register_date datetime not null default current_timestamp);

-- --------add constraints-------
alter table friend_connections
	add constraint connection_fk1 foreign key (user1_id)
		references accounts (id)
        on delete cascade;
alter table friend_connections
	add constraint connection_fk2 foreign key (user2_id)
		references accounts (id)
        on delete cascade;


-- ------------------avatars------------------
create table avatars
(id integer not null auto_increment primary key,
avatar_filename varchar(256) not null,
path_name varchar(256) not null);


alter table avatars
	add constraint unique_avatar unique (avatar_filename, path_name);

alter table accounts
	add constraint avatar_fk foreign key (avatar_id)
    references avatars(id);

    
-- ------------friend_requests----------------
create table friend_requests
(id integer not null auto_increment primary key,
request_sender_id integer not null,
request_reciever_id integer not null);

alter table friend_requests
	add constraint friend_requests_fk1 foreign key (request_sender_id)
	references accounts(id);
	
alter table friend_requests
	add constraint friend_requests_fk2 foreign key (request_reciever_id)
	references accounts(id);
	
	
create table reviews
(id integer not null auto_increment primary key,
review_reciever_id integer not null,
review_point integer not null);

alter table reviews
	add constraint reviews_fk foreign key(review_reciever_id)
	references accounts(id);

-- --------------insert sample values--------------

insert into avatars(avatar_filename, path_name) values ('1.png', "./avatars");
insert into avatars(avatar_filename, path_name) values ('2.png', "./avatars"); 
insert into avatars(avatar_filename, path_name) values ('3.png', "./avatars");
insert into avatars(avatar_filename, path_name) values ('4.png', "./avatars"); 
insert into avatars(avatar_filename, path_name) values ('5.png', "./avatars");
insert into avatars(avatar_filename, path_name) values ('6.png', "./avatars"); 
insert into avatars(avatar_filename, path_name) values ('7.png', "./avatars");
insert into avatars(avatar_filename, path_name) values ('8.png', "./avatars"); 
insert into avatars(avatar_filename, path_name) values ('9.png', "./avatars"); 
    
insert into accounts (user_name, authentication_string, avatar_id)values
	("Sandro", "7f3c0e9cffe87df09efbb7d24fcfe8e4d520125c", 6),
	("Tamo", "b536d821f4dfe6e7168f0a3145583fa2754bd2e3", 1),
    ("Gio", "8bbd45107f60757dfc06f3ecaa75ec7fcf51f5b2", 3),
	("Anano", "8418d17eee4f3bbef294c397e94f08a27f12c257", 1),
	("Izi","596f725f19333604c053fe47bb61d44c514045e5", 2);
	
insert into friend_connections(user1_id, user2_id) values
		(1, 2),
        (3, 1),
        (5, 3),
        (4, 1),
        (5, 2);    
    
drop view if exists friendships;
    
create view friendships
	as
    select distinct f1.user1_id, f1.user2_id from friend_connections f1
    union 
    select distinct f2.user2_id, f2.user1_id from friend_connections f2;

drop view if exists acc_info;

create view acc_info
as
select ac.user_name,
		ac.authentication_string,
        concat(a.path_name, '/',  a.avatar_filename) as full_path
	from accounts ac
    left join avatars a
    on (ac.avatar_id = a.id);
    
    
    