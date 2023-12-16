INSERT INTO users (id, name, password, email, archive, role, bucket_id)
VALUES (1, 'admin', 'admin','mail@mail.com', false, 'ADMIN', null);

ALTER SEQUENCE user_seq RESTART WITH 2;