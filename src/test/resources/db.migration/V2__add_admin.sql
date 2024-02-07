INSERT INTO users (id, name, password, email, archive, role)
VALUES (1, 'admin', '$2y$10$RfekcrdAin3Zw0WiXcDzG.b6Yho0O/QBYsZ83CEpu1ydnCMPp4cW2','mail@mail.com', false, 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;