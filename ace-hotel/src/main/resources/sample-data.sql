-- =====================================================================
-- ACE Hotel - sample-data.sql (H2)
-- Du lieu mau cho demo. CHAY LAI DUOC NHIEU LAN: xoa sach roi nap lai.
-- Ngay dung ngay TUONG DOI so voi hom nay (CURRENT_DATE) nen luon "dung ngay demo".
-- Tai khoan demo: letan / Ace@123 (le tan), quanly / Ace@123 (quan ly).
-- Mat khau chi luu ban bam BCrypt. Chay file schema.sql truoc.
-- =====================================================================

DELETE FROM invoices;
DELETE FROM service_orders;
DELETE FROM bookings;
DELETE FROM services;
DELETE FROM customers;
DELETE FROM rooms;
DELETE FROM room_types;
DELETE FROM users;

-- ---------- users ----------
INSERT INTO users (user_id, username, password_hash, full_name, role) VALUES
  (1, 'letan', '$2b$10$8NTKH/Tw/Ae8Gl2PgmxWs.U5NYuvWwxRVMae6.bEavbb3161vBMD.', 'Lễ tân Demo', 'RECEPTIONIST'),
  (2, 'quanly', '$2b$10$V0pfqw5S/Q2F4F.Tg7HJYuQ3hhClql5veXDxuHJ3K8Poy340qiLDG', 'Quản lý Demo', 'MANAGER');

-- ---------- room_types ----------
INSERT INTO room_types (room_type_id, name, price_per_night, capacity, description) VALUES
  (1, 'Standard', 780000, 2, 'Phòng tiêu chuẩn, giường đôi hoặc hai giường đơn, đầy đủ tiện nghi cơ bản.'),
  (2, 'Superior', 950000, 2, 'Phòng rộng hơn Standard, có cửa sổ lớn và khu làm việc nhỏ.'),
  (3, 'Deluxe', 1250000, 2, 'Phòng cao cấp, nội thất ấm áp, tầm nhìn đẹp.'),
  (4, 'Family', 1800000, 4, 'Phòng gia đình, phù hợp 3 đến 4 khách.'),
  (5, 'Suite', 3200000, 4, 'Căn hộ nghỉ dưỡng gồm phòng ngủ và phòng khách riêng.');

-- ---------- rooms (10 phong, 4 tang, 202 dang bao tri) ----------
INSERT INTO rooms (room_id, room_number, floor, status, room_type_id) VALUES
  (1, '101', 1, 'AVAILABLE', 1),
  (2, '102', 1, 'AVAILABLE', 1),
  (3, '201', 2, 'OCCUPIED', 2),
  (4, '202', 2, 'MAINTENANCE', 2),
  (5, '205', 2, 'AVAILABLE', 1),
  (6, '206', 2, 'AVAILABLE', 2),
  (7, '301', 3, 'OCCUPIED', 3),
  (8, '302', 3, 'AVAILABLE', 3),
  (9, '401', 4, 'AVAILABLE', 4),
  (10, '418', 4, 'OCCUPIED', 5);

-- ---------- customers ----------
INSERT INTO customers (customer_id, full_name, phone, id_number, email) VALUES
  (1, 'Nguyễn Minh Anh', '0901000001', 'DEMO-001', 'minhanh@example.com'),
  (2, 'Trần Quốc Bảo', '0901000002', 'DEMO-002', 'quocbao@example.com'),
  (3, 'Lê Thu Hà', '0901000003', 'DEMO-003', 'thuha@example.com'),
  (4, 'Phạm Gia Huy', '0901000004', 'DEMO-004', 'giahuy@example.com'),
  (5, 'Võ Thanh Tâm', '0901000005', 'DEMO-005', NULL),
  (6, 'Đặng Hoài Nam', '0901000006', 'DEMO-006', 'hoainam@example.com'),
  (7, 'Bùi Ngọc Lan', '0901000007', NULL, 'ngoclan@example.com'),
  (8, 'Hoàng Đức Long', '0901000008', 'DEMO-008', NULL),
  (9, 'Phan Khánh Vy', '0901000009', 'DEMO-009', 'khanhvy@example.com'),
  (10, 'Đỗ Anh Tuấn', '0901000010', NULL, 'anhtuan@example.com');

-- ---------- services ----------
INSERT INTO services (service_id, name, unit_price, unit) VALUES
  (1, 'Ăn sáng', 150000, 'suất'),
  (2, 'Đưa đón sân bay', 300000, 'lượt'),
  (3, 'Giặt ủi', 50000, 'kg'),
  (4, 'Spa', 500000, 'lượt'),
  (5, 'Thuê xe máy', 200000, 'ngày'),
  (6, 'Nước uống', 20000, 'chai');

-- ---------- bookings ----------
-- 1  CHECKED_IN  dang o, nhan hom nay, chua den han tra
-- 2  BOOKED      nhan hom nay (demo check-in)
-- 3  CHECKED_IN  QUA HAN tra phong (check_out_date da qua)
-- 4  BOOKED      nhan ngay mai, thu dat phong 102 trong +2..+4 se bi tu choi (chong lich)
-- 5-7 CHECKED_OUT da tra phong, co hoa don
-- 8  CANCELLED
-- 9-11 BOOKED    cac dat phong sap toi
-- 12 CHECKED_IN  den han tra phong hom nay
INSERT INTO bookings (booking_id, customer_id, room_id, check_in_date, check_out_date, status, actual_check_in, actual_check_out, created_by, created_at) VALUES
  (1, 1, 7, DATEADD('DAY', 0, CURRENT_DATE), DATEADD('DAY', 2, CURRENT_DATE), 'CHECKED_IN', DATEADD('HOUR', 14, CAST(DATEADD('DAY', 0, CURRENT_DATE) AS TIMESTAMP)), NULL, 1, DATEADD('HOUR', 10, CAST(DATEADD('DAY', -2, CURRENT_DATE) AS TIMESTAMP))),
  (2, 2, 5, DATEADD('DAY', 0, CURRENT_DATE), DATEADD('DAY', 1, CURRENT_DATE), 'BOOKED', NULL, NULL, 1, DATEADD('HOUR', 9, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (3, 3, 10, DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), 'CHECKED_IN', DATEADD('HOUR', 14, CAST(DATEADD('DAY', -3, CURRENT_DATE) AS TIMESTAMP)), NULL, 1, DATEADD('HOUR', 15, CAST(DATEADD('DAY', -5, CURRENT_DATE) AS TIMESTAMP))),
  (4, 4, 2, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', 3, CURRENT_DATE), 'BOOKED', NULL, NULL, 2, DATEADD('HOUR', 16, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (5, 5, 3, DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', -7, CURRENT_DATE), 'CHECKED_OUT', DATEADD('HOUR', 14, CAST(DATEADD('DAY', -10, CURRENT_DATE) AS TIMESTAMP)), DATEADD('HOUR', 11, CAST(DATEADD('DAY', -7, CURRENT_DATE) AS TIMESTAMP)), 1, DATEADD('HOUR', 9, CAST(DATEADD('DAY', -14, CURRENT_DATE) AS TIMESTAMP))),
  (6, 6, 8, DATEADD('DAY', -6, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), 'CHECKED_OUT', DATEADD('HOUR', 15, CAST(DATEADD('DAY', -6, CURRENT_DATE) AS TIMESTAMP)), DATEADD('HOUR', 11, CAST(DATEADD('DAY', -4, CURRENT_DATE) AS TIMESTAMP)), 2, DATEADD('HOUR', 10, CAST(DATEADD('DAY', -9, CURRENT_DATE) AS TIMESTAMP))),
  (7, 7, 1, DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), 'CHECKED_OUT', DATEADD('HOUR', 14, CAST(DATEADD('DAY', -5, CURRENT_DATE) AS TIMESTAMP)), DATEADD('HOUR', 12, CAST(DATEADD('DAY', -3, CURRENT_DATE) AS TIMESTAMP)), 1, DATEADD('HOUR', 14, CAST(DATEADD('DAY', -8, CURRENT_DATE) AS TIMESTAMP))),
  (8, 8, 9, DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', 7, CURRENT_DATE), 'CANCELLED', NULL, NULL, 1, DATEADD('HOUR', 11, CAST(DATEADD('DAY', -3, CURRENT_DATE) AS TIMESTAMP))),
  (9, 9, 6, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE), 'BOOKED', NULL, NULL, 1, DATEADD('HOUR', 11, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (10, 1, 8, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE), 'BOOKED', NULL, NULL, 2, DATEADD('HOUR', 8, CAST(DATEADD('DAY', 0, CURRENT_DATE) AS TIMESTAMP))),
  (11, 6, 1, DATEADD('DAY', 0, CURRENT_DATE), DATEADD('DAY', 2, CURRENT_DATE), 'BOOKED', NULL, NULL, 1, DATEADD('HOUR', 17, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (12, 7, 3, DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 0, CURRENT_DATE), 'CHECKED_IN', DATEADD('HOUR', 14, CAST(DATEADD('DAY', -2, CURRENT_DATE) AS TIMESTAMP)), NULL, 1, DATEADD('HOUR', 10, CAST(DATEADD('DAY', -4, CURRENT_DATE) AS TIMESTAMP)));

-- ---------- service_orders (don gia sao chep tu services.unit_price) ----------
INSERT INTO service_orders (order_id, booking_id, service_id, quantity, unit_price, ordered_at) VALUES
  (1, 1, 1, 2, 150000, DATEADD('HOUR', 15, CAST(DATEADD('DAY', 0, CURRENT_DATE) AS TIMESTAMP))),
  (2, 1, 6, 3, 20000, DATEADD('HOUR', 18, CAST(DATEADD('DAY', 0, CURRENT_DATE) AS TIMESTAMP))),
  (3, 3, 4, 1, 500000, DATEADD('HOUR', 16, CAST(DATEADD('DAY', -2, CURRENT_DATE) AS TIMESTAMP))),
  (4, 3, 3, 3, 50000, DATEADD('HOUR', 9, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (5, 12, 3, 2, 50000, DATEADD('HOUR', 10, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (6, 12, 1, 1, 150000, DATEADD('HOUR', 8, CAST(DATEADD('DAY', -1, CURRENT_DATE) AS TIMESTAMP))),
  (7, 5, 1, 3, 150000, DATEADD('HOUR', 8, CAST(DATEADD('DAY', -9, CURRENT_DATE) AS TIMESTAMP))),
  (8, 5, 2, 1, 300000, DATEADD('HOUR', 9, CAST(DATEADD('DAY', -7, CURRENT_DATE) AS TIMESTAMP))),
  (9, 6, 5, 2, 200000, DATEADD('HOUR', 9, CAST(DATEADD('DAY', -5, CURRENT_DATE) AS TIMESTAMP)));

-- ---------- invoices (tien phong = so dem x gia, tong = tien phong + dich vu) ----------
INSERT INTO invoices (invoice_id, booking_id, room_charge, service_charge, total, payment_method, paid_at, created_by) VALUES
  (1, 5, 2850000, 750000, 3600000, 'CASH', DATEADD('HOUR', 11, CAST(DATEADD('DAY', -7, CURRENT_DATE) AS TIMESTAMP)), 1),
  (2, 6, 2500000, 400000, 2900000, 'TRANSFER', DATEADD('HOUR', 11, CAST(DATEADD('DAY', -4, CURRENT_DATE) AS TIMESTAMP)), 2),
  (3, 7, 1560000, 0, 1560000, 'CARD', DATEADD('HOUR', 12, CAST(DATEADD('DAY', -3, CURRENT_DATE) AS TIMESTAMP)), 1);

-- ---------- dat lai bo dem tu tang (de ung dung them du lieu moi khong bi trung ID) ----------
ALTER TABLE users          ALTER COLUMN user_id      RESTART WITH 3;
ALTER TABLE room_types     ALTER COLUMN room_type_id RESTART WITH 6;
ALTER TABLE rooms          ALTER COLUMN room_id      RESTART WITH 11;
ALTER TABLE customers      ALTER COLUMN customer_id  RESTART WITH 11;
ALTER TABLE services       ALTER COLUMN service_id   RESTART WITH 7;
ALTER TABLE bookings       ALTER COLUMN booking_id   RESTART WITH 13;
ALTER TABLE service_orders ALTER COLUMN order_id     RESTART WITH 10;
ALTER TABLE invoices       ALTER COLUMN invoice_id   RESTART WITH 4;
