SET REFERENTIAL_INTEGRITY FALSE;
truncate table post_it;
truncate table room;
truncate table sixhat;
truncate table tool;
truncate table user;
-- ALTER TABLE post_it AUTO_INCREMENT=1;
-- ALTER TABLE room AUTO_INCREMENT=1;
-- ALTER TABLE sixhat AUTO_INCREMENT=1;
-- ALTER TABLE tool AUTO_INCREMENT=1;
-- ALTER TABLE users AUTO_INCREMENT=1;
SET REFERENTIAL_INTEGRITY TRUE;

insert into user (`id`, `userid`, `username`, `userpw`)
values (1, 'HA', 'HA', 'HA'),
       (2, 'LEE', 'LEE', 'LEE');