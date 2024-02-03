INSERT INTO products(id, price, title)
VALUES (1, 120.0, 'T-Shirt'),
       (2, 160.0, 'Hoodie'),
       (3, 250.0, 'Jacket'),
       (4, 360.0, 'Shoes'),
       (5, 60.0, 'Glasses');

ALTER SEQUENCE product_seq RESTART WITH 6;