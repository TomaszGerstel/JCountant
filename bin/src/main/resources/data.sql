

insert into `user_role` (name, description) values ('USER', 'basic authentication role');
insert into `user_role` (name, description) values ('ADMIN', 'admin authentication role');
insert into `user` (username, email, password) values ('admin', 'dm@gm.com','$2a$10$pxWU5Yhaf1gPACSjbanCsOGHMjJSTTeFrJiVle.3CChWnVALXW6lq');
insert into `user_roles` (user_id, role_id) values (1, 1);