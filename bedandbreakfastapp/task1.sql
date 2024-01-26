drop database if exists bedandbreakfast;

create database bedandbreakfast;

use bedandbreakfast;


create table users(

    email varchar(128),
    name varchar(128),

    primary key (email)
);

create table bookings(

    booking_id char(8),
    listing_id varchar(20),
    duration int,
    email varchar(128),

    primary key(booking_id),
    constraint fk_email foreign key(email) references users(email)
);


create table reviews(

    id int auto_increment,
    date timestamp default current_timestamp,
    listing_id varchar(20),
    reviewer_name varchar(64),
    comments text,

    primary key(id)
    -- https://stackoverflow.com/questions/72359211/error-code-1822-failed-to-add-the-foreign-key-constraint-missing-index-for-co
);

SET GLOBAL local_infile = TRUE;
-- LOAD DATA INFILE "C:\ProgramData\MySQL\MySQL Server 8.0\Uploads\users.csv"
LOAD DATA LOCAL INFILE 'C:\Users\tammy\OneDrive\Documents\VTTP\workshop-paf\paf_assessment_template\data\users.csv'
INTO TABLE users
FIELDS TERMINATED BY ',';


-- SELECT *
-- FROM users
-- INTO OUTFILE 'C:\ProgramData\MySQL\MySQL Server 8.0\Uploads\users.csv'
--     FIELDS TERMINATED BY ',';