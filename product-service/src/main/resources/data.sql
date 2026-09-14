INSERT INTO catalog_products (id, name, description, price, category, active, created_at)
VALUES (1, 'Laptop Rikkei Pro 14', 'Laptop 14 inch, RAM 16GB, SSD 512GB', 24990000.00, 'LAPTOP', true, CURRENT_TIMESTAMP),
       (2, 'Tai nghe Bluetooth AirBeat', 'Tai nghe chống ồn chủ động', 1890000.00, 'ACCESSORY', true, CURRENT_TIMESTAMP),
       (3, 'Bàn phím cơ RK87', 'Bàn phím cơ không dây TKL', 1290000.00, 'ACCESSORY', true, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE name = VALUES(name), price = VALUES(price), active = VALUES(active);

