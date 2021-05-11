create table employee (
    id char(36) default (uuid()) not null primary key,
    username varchar(20),
    full_name varchar(255),
    date_of_birth varchar(255),
    age integer,
    status integer
)