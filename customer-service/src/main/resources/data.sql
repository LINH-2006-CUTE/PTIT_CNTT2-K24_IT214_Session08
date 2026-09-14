INSERT INTO customer_profiles (id, full_name, email, phone, address, created_at)
VALUES (1, 'Nguyễn Minh Anh', 'minhanh@example.com', '0901234567', 'Cầu Giấy, Hà Nội', CURRENT_TIMESTAMP),
       (2, 'Trần Quốc Bảo', 'quocbao@example.com', '0912345678', 'Quận 1, TP. Hồ Chí Minh', CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

