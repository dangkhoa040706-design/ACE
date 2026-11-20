-- =========================================================================
-- ACE HOTEL - RESET & SEED SAMPLE DATA (WITH ROOM_ID LOCKED ON BOOKINGS)
-- =========================================================================

DELETE FROM invoices;
DELETE FROM service_orders;
DELETE FROM bookings;
DELETE FROM customers;
DELETE FROM rooms;
DELETE FROM services;
DELETE FROM room_types;
DELETE FROM users;

-- 1. USERS (Tài khoản chuẩn theo 3 Role)
--   - Admin:      admin / admin
--   - Lễ tân 1:   nhanvien / nhanvien
--   - Lễ tân 2:   letan / Letan@2026
--   - Khách hàng: khach / khach
INSERT INTO users (user_id, username, password_hash, full_name, role) VALUES
  (1, 'admin',    '$2a$10$0Ugq77DTSjlWW07fSrqrJOT9vs09zmrO0/YhyGpRSUifVF/zPLQiu', 'Quản Trị Viên Hệ Thống', 'ADMIN'),
  (2, 'nhanvien', '$2a$10$zROSeQ5G9MPvwN3/6SJUju8hm3NrvE2xZdblUWB84vdfgdhXE8rqS', 'Nhân Viên Lễ Tân PMS',    'RECEPTIONIST'),
  (3, 'khach',    '$2a$10$O/OXdKwKVeaLCFl2BNdC8uLW.V7JhNrx/0bNsvZ79EdfnjyZY.OCa', 'Khách Hàng Thân Thiết',  'CUSTOMER'),
  (4, 'letan',    '$2a$10$YNx4kyF1elFm1J6Yy6A5de9Qrrf.Pqn41SlGZ/CQeE7gURbNsjzIu', 'Lễ Tân Tiếp Đón PMS',      'RECEPTIONIST');

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 10;

-- 2. ROOM_TYPES (5 Hạng phòng: Giá ngày thường T2–T5 & Giá cuối tuần T6–CN)
INSERT INTO room_types (room_type_id, name, price_per_night, weekend_price_per_night, capacity, bed_type, description, image_url) VALUES
  (1, 'Standard', 780000,  950000,  2, '1 Giường đôi Queen (1m6 × 2m)', 'Phòng tiêu chuẩn giường đôi, đầy đủ tiện nghi cơ bản', '/images/room-standard.png'),
  (2, 'Superior', 950000,  1150000, 2, '1 Giường đôi King (1m8 × 2m)',  'Phòng diện tích rộng, cửa sổ thoáng nhìn ra phố',       '/images/room-superior.png'),
  (3, 'Deluxe',   1000000, 1250000, 2, '1 Giường King lớn (2m × 2m2) ban công',     'Phòng cao cấp ban công riêng, nội thất sang trọng',     '/images/room-deluxe.png'),
  (4, 'Family',   1850000, 2200000, 4, '2 Giường đôi Queen lớn (1m6 × 2m)',         'Phòng gia đình 2 giường lớn, không gian sinh hoạt chung','/images/room-family.png'),
  (5, 'Suite',    3200000, 3800000, 4, '1 Giường King Master + 1 Sofa Bed cao cấp', 'Phòng hạng sang có phòng khách riêng biệt, bồn tắm nằm','/images/room-suite.png');

ALTER TABLE room_types ALTER COLUMN room_type_id RESTART WITH 10;

-- 3. ROOMS (12 Phòng trên 4 tầng: 9 AVAILABLE, 2 OCCUPIED, 1 DIRTY)
INSERT INTO rooms (room_id, room_number, floor, status, room_type_id) VALUES
  (1,  '101', 1, 'AVAILABLE', 1),
  (2,  '102', 1, 'DIRTY',     1),
  (3,  '103', 1, 'AVAILABLE', 2),
  (4,  '201', 2, 'AVAILABLE', 3),
  (5,  '202', 2, 'OCCUPIED',  2),
  (6,  '205', 2, 'AVAILABLE', 1),
  (7,  '301', 3, 'OCCUPIED',  3),
  (8,  '302', 3, 'AVAILABLE', 3),
  (9,  '305', 3, 'AVAILABLE', 4),
  (10, '401', 4, 'AVAILABLE', 4),
  (11, '418', 4, 'AVAILABLE', 5),
  (12, '420', 4, 'AVAILABLE', 5);

ALTER TABLE rooms ALTER COLUMN room_id RESTART WITH 20;

-- 4. SERVICES (8 Dịch vụ thuộc 4 nhóm: MINIBAR, LAUNDRY, FB, DAMAGES)
INSERT INTO services (service_id, name, category, unit_price, unit) VALUES
  (1, 'Nước ngọt & Bia Tiger lon',              'MINIBAR', 35000,  'Lon'),
  (2, 'Combo Snack & Hạt điều cao cấp',         'MINIBAR', 50000,  'Gói'),
  (3, 'Giặt ủi nhanh bộ trang phục',            'LAUNDRY', 80000,  'Bộ'),
  (4, 'Giặt hấp / Giặt khô Áo Vest',            'LAUNDRY', 150000, 'Chiếc'),
  (5, 'Bữa sáng Buffet gọi thêm',               'FB',      100000, 'Suất'),
  (6, 'Phục vụ món nóng tại phòng (In-room)',   'FB',      220000, 'Phần'),
  (7, 'Bồi thường vỡ ly thủy tinh / cốc sứ',   'DAMAGES', 100000, 'Cái'),
  (8, 'Bồi thường mất thẻ từ chìa khóa phòng', 'DAMAGES', 200000, 'Thẻ');

ALTER TABLE services ALTER COLUMN service_id RESTART WITH 20;

-- 5. CUSTOMERS
INSERT INTO customers (customer_id, user_id, full_name, date_of_birth, phone, id_number, email) VALUES
  (1, 3,    'Khách Hàng Thân Thiết', DATE '2000-01-01', '0901234567', '079201001234', 'khach@acehotel.vn'),
  (2, NULL, 'Trần Quốc Bảo',         DATE '1999-11-20', '0912345678', '079200005678', 'quocbao.tran@gmail.com'),
  (3, NULL, 'Phạm Gia Huy',          NULL,              '0933445566', '079202008899', NULL),
  (4, NULL, 'Đặng Minh Khôi',        DATE '1995-07-22', '0944556677', '079195003344', 'minhkhoi.dang@gmail.com');

ALTER TABLE customers ALTER COLUMN customer_id RESTART WITH 20;

-- 6. BOOKINGS (4 đơn chuẩn theo logic mới: luôn có room_id đã chọn)
INSERT INTO bookings (
  booking_id, booking_code, customer_id, room_type_id, room_id, guest_count,
  check_in_date, check_out_date, actual_check_in, actual_check_out,
  room_charge, deposit_amount, early_checkin_fee, late_checkout_fee, status, created_by
) VALUES
  -- BK-2401: Khách Online (khach) đang ở P.301 (Deluxe, 2 đêm = 2.000.000đ, đã cọc 30% = 693.000đ)
  (2401, 'BK-2401', 1, 3, 7, 2,
   DATEADD('DAY', -2, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL,
   2000000, 693000, 0, 0, 'CHECKED_IN', 3),

  -- BK-2402: Khách Online đặt trước và đã chốt P.201 (Deluxe, đã cọc 30% = 693.000đ) -> Chờ Check-in
  (2402, 'BK-2402', 2, 3, 4, 2,
   CURRENT_DATE, DATEADD('DAY', 2, CURRENT_DATE), NULL, NULL,
   2000000, 693000, 0, 0, 'RESERVED', 3),

  -- BK-2405: Khách Walk-in đang ở P.202 (Superior, 1 đêm = 950.000đ, cọc 0đ)
  (2405, 'BK-2405', 3, 2, 5, 2,
   DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL,
   950000, 0, 0, 0, 'CHECKED_IN', 2),

  -- BK-2406: Khách đã trả phòng P.401 (Family, 2 đêm = 3.700.000đ), đã có Hóa đơn INV-1
  (2406, 'BK-2406', 4, 4, 10, 4,
   DATEADD('DAY', -4, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE),
   DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP),
   3700000, 1282050, 0, 0, 'CHECKED_OUT', 2);

ALTER TABLE bookings ALTER COLUMN booking_id RESTART WITH 2500;

-- 7. SERVICE_ORDERS
INSERT INTO service_orders (order_id, booking_id, service_id, quantity, unit_price, ordered_at, created_by) VALUES
  (1, 2401, 5, 2, 100000, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 2),
  (2, 2401, 3, 1, 80000,  DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 2),
  (3, 2406, 5, 2, 100000, DATEADD('DAY',  -3,  CURRENT_TIMESTAMP), 2),
  (4, 2406, 7, 1, 100000, DATEADD('DAY',  -2,  CURRENT_TIMESTAMP), 2);

ALTER TABLE service_orders ALTER COLUMN order_id RESTART WITH 50;

-- 8. INVOICES
INSERT INTO invoices (
  invoice_id, booking_id, room_charge, service_charge_total, early_checkin_fee, late_checkout_fee,
  subtotal, service_charge_amount, vat_amount, grand_total, deposit_deducted, balance_due,
  payment_method, paid_at, created_by
) VALUES
  (1, 2406, 3700000, 300000, 0, 0,
   4000000, 200000, 420000, 4620000, 1282050, 3337950,
   'TRANSFER', DATEADD('DAY', -2, CURRENT_TIMESTAMP), 2);

ALTER TABLE invoices ALTER COLUMN invoice_id RESTART WITH 10;
