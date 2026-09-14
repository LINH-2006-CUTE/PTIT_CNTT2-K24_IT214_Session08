INSERT INTO shop_orders (id, customer_id, status, total_amount, created_at)
VALUES (1, 1, 'PAID', 28770000.00, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE customer_id = VALUES(customer_id);

INSERT INTO order_items (id, order_id, product_id, product_name, quantity, unit_price, subtotal)
VALUES (1, 1, 1, 'Laptop Rikkei Pro 14', 1, 24990000.00, 24990000.00),
       (2, 1, 2, 'Tai nghe Bluetooth AirBeat', 2, 1890000.00, 3780000.00)
ON DUPLICATE KEY UPDATE order_id = VALUES(order_id);

