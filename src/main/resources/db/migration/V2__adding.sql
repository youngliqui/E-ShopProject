INSERT INTO users (id, name, password, email, archive, role)
VALUES (1, 'admin', '$2y$10$RfekcrdAin3Zw0WiXcDzG.b6Yho0O/QBYsZ83CEpu1ydnCMPp4cW2', 'mail@mail.com', false, 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;

INSERT INTO products(id, price, title, description, sale, size, color, care_instructions, weight, material, quantity)
VALUES (1, 120.0, 'T-Shirt', 'basic t-shirt for boys', 0, 'XL', 'white',
        'Wash the T-shirt inside out with like colors in cold water to prevent shrinking and fading.' ||
        'Use a mild detergent and avoid using bleach or fabric softener.' ||
        'Hang or lay flat to dry to prevent wrinkles.' ||
        'Iron the T-shirt inside out on a low setting to avoid damaging the fabric.' ||
        'Store the T-shirt folded to prevent wrinkles and stretching.', 50.0, '100% combed ringspun cotton', 10),
       (2, 160.0, 'Hoodie', 'hoodie street style', 5, 'S', 'black',
        'Wash the hoodie inside out with like colors in cold water.' ||
        'Use a mild detergent and avoid using bleach or fabric softener.' ||
        'Hang or lay flat to dry to prevent shrinking and wrinkles.' ||
        'Iron the hoodie inside out on a low setting to avoid damaging the fabric.' ||
        'Store the hoodie folded to prevent wrinkles and stretching.', 150.0, '80% ringspun cotton, 20% polyester', 5),
       (3, 250.0, 'Jacket', 'jacket for women', 10, 'M', 'yellow',
        'Check the care label for specific instructions.' ||
        'Dry clean or wash the jacket according to the label instructions.' ||
        'Hang the jacket on a padded hanger to prevent stretching.' ||
        'Store the jacket in a garment bag or on a hanger to prevent wrinkles and dust.' ||
        'Spot clean the jacket as needed to remove stains.',
        300.0, 'Waterproof nylon with polyester fill', 2),
       (4, 360.0, 'Shoes', 'elegant sneaks', 0, 'XXL', 'matte black',
        'Clean the shoes with a soft brush and shoe cleaner.' ||
        'Stuff the shoes with newspaper to help them keep their shape.' ||
        'Use a shoe tree to prevent creases and odors.' ||
        'Store the shoes in a cool, dry place away from direct sunlight.' ||
        'Polish the shoes regularly to keep them looking new.', 400.0, 'Leather with rubber sole', 1),
       (5, 60.0, 'Glasses', 'sunglasses', 0, 'M', 'gray',
        'Clean the glasses with a microfiber cloth and lens cleaning solution.' ||
        'Store the glasses in a hard case to prevent scratches and damage.' ||
        'Adjust the nose pads and temple arms for a comfortable fit.' ||
        'Avid leaving the glasses in hot or cold temperatures.' ||
        'Replace the lenses if they become scratched or damaged.', 50.0, ' Acetate or metal frame with CR-39 lenses', 8);

ALTER SEQUENCE product_seq RESTART WITH 6;