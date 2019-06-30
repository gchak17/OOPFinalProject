drop database if exists paint;

create database paint;

use paint;

drop table if exists accounts;

-- ------------------accounts table--------------
create table accounts
(id integer auto_increment primary key not null,
 user_name text not null,
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
create table avatar_paths
(id integer not null auto_increment primary key,
pathname text not null);

create table avatars
(id integer not null auto_increment primary key,
avatar_filename text not null,
relative_path_id integer not null);

alter table avatars
	add constraint pathname_fk foreign key(relative_path_id)
		references avatar_paths(id);

alter table accounts
	add constraint avatar_fk foreign key (avatar_id)
    references avatars(id);


-- --------------insert sample values--------------
insert into avatar_paths(pathname) values ('.');
insert into avatar_paths(pathname) values ('./avatars');


insert into avatars(avatar_filename, relative_path_id) values ('1.png', 2);
insert into avatars(avatar_filename, relative_path_id) values ('2.png', 2); 
insert into avatars(avatar_filename, relative_path_id) values ('3.png', 2);
insert into avatars(avatar_filename, relative_path_id) values ('4.png', 2); 
insert into avatars(avatar_filename, relative_path_id) values ('5.png', 2);
insert into avatars(avatar_filename, relative_path_id) values ('6.png', 2); 
insert into avatars(avatar_filename, relative_path_id) values ('7.png', 2);
insert into avatars(avatar_filename, relative_path_id) values ('8.png', 2); 
insert into avatars(avatar_filename, relative_path_id) values ('9.png', 2); 
    
insert into accounts (user_name, authentication_string, avatar_id)values
	("Sandro", "Sandro", 6),
	("Tamo", "Tamo", 1),
    ("Gio", "Gio", 3),
	("Anano", "Anano", 1),
	("Izi","Izi", 2);
	
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
        pathname,
        avatar_filename
	from accounts ac
    left join avatars a
    on (ac.avatar_id = a.id)
    left join avatar_paths ap
    on (a.relative_path_id = ap.id);

create view avatar_info
as
select a.id,
		a.avatar_filename,
        ap.pathname
	from avatars a
    join avatar_paths ap
    on (a.relative_path_id = ap.id);
    







    