INSERT INTO payments (id, order_id, amount, method, status, paid_at)
VALUES (1, 1, 28770000.00, 'BANK_TRANSFER', 'PAID', CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE order_id = VALUES(order_id);

