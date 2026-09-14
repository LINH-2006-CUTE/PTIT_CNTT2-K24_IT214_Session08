INSERT INTO stocks (id, product_id, available_quantity, updated_at, version)
VALUES (1, 1, 25, CURRENT_TIMESTAMP, 0),
       (2, 2, 100, CURRENT_TIMESTAMP, 0),
       (3, 3, 60, CURRENT_TIMESTAMP, 0)
ON DUPLICATE KEY UPDATE product_id = VALUES(product_id);

