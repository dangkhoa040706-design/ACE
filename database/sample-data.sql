-- =========================================================================
-- ACE HOTEL - RESET & SEED SAMPLE DATA (RELATIVE TO CURRENT_DATE)
-- =========================================================================

DELETE FROM invoices;
DELETE FROM service_orders;
DELETE FROM bookings;
DELETE FROM customers;
DELETE FROM rooms;
DELETE FROM services;
DELETE FROM room_types;
DELETE FROM users;

-- 1. USERS (Đúng 3 tài khoản chuẩn duy nhất theo 3 Role)
--   - Admin:      admin / admin
--   - Lễ tân:     nhanvien / nhanvien
--   - Khách hàng: khach / khach
INSERT INTO users (user_id, username, password_hash, full_name, role) VALUES
  (1, 'admin',    '$2a$10$0Ugq77DTSjlWW07fSrqrJOT9vs09zmrO0/YhyGpRSUifVF/zPLQiu', 'Quản Trị Viên Hệ Thống', 'ADMIN'),
  (2, 'nhanvien', '$2a$10$zROSeQ5G9MPvwN3/6SJUju8hm3NrvE2xZdblUWB84vdfgdhXE8rqS', 'Nhân Viên Lễ Tân PMS',    'RECEPTIONIST'),
  (3, 'khach',    '$2a$10$O/OXdKwKVeaLCFl2BNdC8uLW.V7JhNrx/0bNsvZ79EdfnjyZY.OCa', 'Khách Hàng Thân Thiết',  'CUSTOMER');

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 10;

-- 2. ROOM_TYPES (5 Hạng phòng: Giá ngày thường T2–T5 & Giá cuối tuần T6–CN)
INSERT INTO room_types (room_type_id, name, price_per_night, weekend_price_per_night, capacity, bed_type, description, image_url) VALUES
  (1, 'Standard', 780000,  950000,  2, '1 Giường đôi Queen (1m6) hoặc 2 Giường đơn', 'Phòng tiêu chuẩn giường đôi, đầy đủ tiện nghi cơ bản', '/images/room-standard.png'),
  (2, 'Superior', 950000,  1150000, 2, '1 Giường đôi King (1m8) hoặc 2 Giường đơn', 'Phòng diện tích rộng, cửa sổ thoáng nhìn ra phố',       '/images/room-superior.png'),
  (3, 'Deluxe',   1000000, 1250000, 2, '1 Giường King lớn (2m × 2m2) ban công',     'Phòng cao cấp ban công riêng, nội thất sang trọng',     '/images/room-deluxe.png'),
  (4, 'Family',   1850000, 2200000, 4, '2 Giường đôi Queen lớn (1m6 × 2m)',         'Phòng gia đình 2 giường lớn, không gian sinh hoạt chung','/images/room-family.png'),
  (5, 'Suite',    3200000, 3800000, 4, '1 Giường King Master + 1 Sofa Bed cao cấp', 'Phòng hạng sang có phòng khách riêng biệt, bồn tắm nằm','/images/room-suite.png');

ALTER TABLE room_types ALTER COLUMN room_type_id RESTART WITH 10;

-- 3. ROOMS (12 Phòng trên 4 tầng - Đủ 4 trạng thái: 6 AVAILABLE, 3 OCCUPIED, 2 DIRTY, 1 MAINTENANCE)
INSERT INTO rooms (room_id, room_number, floor, status, room_type_id) VALUES
  (1,  '101', 1, 'AVAILABLE',   1), -- Standard trống sạch -> Demo Walk-in 0đ cọc
  (2,  '102', 1, 'DIRTY',       1), -- Standard chờ dọn -> Demo nút [Xác nhận đã dọn sạch]
  (3,  '103', 1, 'MAINTENANCE', 2), -- Superior bảo trì -> Loại khỏi mẫu số Occupancy Rate
  (4,  '201', 2, 'AVAILABLE',   3), -- Deluxe trống sạch -> Demo Gán phòng cho BK-2402
  (5,  '202', 2, 'OCCUPIED',    2), -- Superior đang ở (Khách Walk-in BK-2405, cọc 0đ)
  (6,  '205', 2, 'AVAILABLE',   1), -- Standard trống sạch
  (7,  '301', 3, 'OCCUPIED',    3), -- Deluxe đang ở (Khách Online BK-2401, đã cọc 30%)
  (8,  '302', 3, 'AVAILABLE',   3), -- Deluxe trống sạch
  (9,  '305', 3, 'AVAILABLE',   4), -- Family trống sạch -> Demo Admin bật/tắt Bảo trì
  (10, '401', 4, 'DIRTY',       4), -- Family vừa trả phòng (BK-2406) -> Chặn xóa phòng
  (11, '418', 4, 'OCCUPIED',    5), -- Suite đang ở quá hạn (BK-2403)
  (12, '420', 4, 'AVAILABLE',   5); -- Suite trống sạch

ALTER TABLE rooms ALTER COLUMN room_id RESTART WITH 20;

-- 4. SERVICES (8 Dịch vụ thuộc 4 nhóm: MINIBAR, LAUNDRY, FB, DAMAGES)
INSERT INTO services (service_id, name, category, unit_price, unit) VALUES
  (1, 'Nước ngọt & Bia Tiger lon',              'MINIBAR', 35000,  'Lon'),
  (2, 'Combo Snack & Hạt điều cao cấp',         'MINIBAR', 50000,  'Gói'),
  (3, 'Giặt ủi nhanh bộ trang phục',            'LAUNDRY', 80000,  'Bộ'),
  (4, 'Giặt hấp / Giặt khô Áo Vest',            'LAUNDRY', 150000, 'Chiếc'),
  (5, 'Bữa sáng Buffet gọi thêm',               'FB',      100000, 'Suất'),
  (6, 'Phục vụ món nóng tại phòng (In-room)',   'FB',      220000, 'Phần'),
  (7, 'Đền bù vỡ ly thủy tinh / cốc sứ',        'DAMAGES', 100000, 'Cái'),
  (8, 'Đền bù mất thẻ từ chìa khóa phòng',      'DAMAGES', 200000, 'Thẻ');

ALTER TABLE services ALTER COLUMN service_id RESTART WITH 20;

-- 5. CUSTOMERS (1 Khách hàng tài khoản trực tuyến (khach) & 5 Khách hồ sơ lưu trú)
INSERT INTO customers (customer_id, user_id, full_name, date_of_birth, phone, id_number, email) VALUES
  (1, 3,    'Khách Hàng Thân Thiết', DATE '2000-01-01', '0901234567', '079201001234', 'khach@acehotel.vn'),
  (2, NULL, 'Trần Quốc Bảo',         DATE '1999-11-20', '0912345678', '079200005678', 'quocbao.tran@gmail.com'),
  (3, NULL, 'Lê Thu Hà',             DATE '1998-03-08', '0987654321', '001199004321', 'thuha.le@gmail.com'),
  (4, NULL, 'Phạm Gia Huy',          NULL,              '0933445566', '079202008899', NULL), -- Khách Walk-in
  (5, NULL, 'Đặng Minh Khôi',        DATE '1995-07-22', '0944556677', '079195003344', 'minhkhoi.dang@gmail.com'),
  (6, NULL, 'Bùi Thanh Trúc',        DATE '2003-09-10', '0966112233', '082203007788', 'thanhtruc.bui@gmail.com');

ALTER TABLE customers ALTER COLUMN customer_id RESTART WITH 20;

-- 6. BOOKINGS (6 Đơn đặt phòng mẫu phủ đủ các tình huống Demo)
INSERT INTO bookings (
  booking_id, booking_code, customer_id, room_type_id, room_id, guest_count,
  check_in_date, check_out_date, actual_check_in, actual_check_out,
  room_charge, deposit_amount, early_checkin_fee, late_checkout_fee, status, created_by
) VALUES
  -- BK-2401: Khách Online (khach) đang ở P.301 (Deluxe, 2 đêm = 2.000.000đ, đã cọc 30% của 2.310k = 693.000đ)
  (2401, 'BK-2401', 1, 3, 7, 2,
   DATEADD('DAY', -2, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL,
   2000000, 693000, 0, 0, 'CHECKED_IN', 3),

  -- BK-2402: Khách Online đặt hạng Deluxe hôm nay (room_id = NULL, đã cọc 30% = 693.000đ) -> Chờ quét QR gán phòng P.201
  (2402, 'BK-2402', 2, 3, NULL, 2,
   CURRENT_DATE, DATEADD('DAY', 2, CURRENT_DATE), NULL, NULL,
   2000000, 693000, 0, 0, 'RESERVED', 3),

  -- BK-2403: Khách Lê Thu Hà ở P.418 (Suite) quá hạn trả phòng 1 ngày (OVERDUE)
  (2403, 'BK-2403', 3, 5, 11, 2,
   DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_TIMESTAMP), NULL,
   6400000, 2217600, 0, 0, 'CHECKED_IN', 2),

  -- BK-2404: Khách Online Nguyễn Minh Anh đặt trước hạng Standard tuần sau (RESERVED, room_id = NULL) -> Hiện trên Customer Portal & Demo Hủy
  (2404, 'BK-2404', 1, 1, NULL, 2,
   DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE), NULL, NULL,
   1560000, 540540, 0, 0, 'RESERVED', 3),

  -- BK-2405: Khách Walk-in Phạm Gia Huy đang ở P.202 (Superior, 1 đêm = 950.000đ, deposit_amount = 0đ) -> Demo Check-out Walk-in thu 100%
  (2405, 'BK-2405', 4, 2, 5, 2,
   DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL,
   950000, 0, 0, 0, 'CHECKED_IN', 2),

  -- BK-2406: Khách Đặng Minh Khôi đã trả phòng P.401 (Family, 2 đêm = 3.700.000đ), đã có Hóa đơn INV-1
  (2406, 'BK-2406', 5, 4, 10, 4,
   DATEADD('DAY', -4, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE),
   DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP),
   3700000, 1282050, 0, 0, 'CHECKED_OUT', 2);

ALTER TABLE bookings ALTER COLUMN booking_id RESTART WITH 2500;

-- 7. SERVICE_ORDERS (Room Folio: BK-2401 đang có đúng 300.000đ dịch vụ để khớp ví dụ quyết toán 2.656.500đ)
INSERT INTO service_orders (order_id, booking_id, service_id, quantity, unit_price, ordered_at, created_by) VALUES
  (1, 2401, 5, 2, 100000, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 2), -- 2 Bữa sáng Buffet = 200.000đ
  (2, 2401, 3, 1, 80000,  DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 2), -- 1 Giặt ủi nhanh   =  80.000đ
  (3, 2401, 1, 1, 20000,  DATEADD('HOUR', -5,  CURRENT_TIMESTAMP), 2), -- 1 Nước ngọt lon   =  20.000đ (Tổng BK-2401 = 300.000đ)
  (4, 2403, 6, 2, 220000, DATEADD('DAY',  -2,  CURRENT_TIMESTAMP), 2), -- 2 Món nóng P.418  = 440.000đ
  (5, 2406, 5, 2, 100000, DATEADD('DAY',  -3,  CURRENT_TIMESTAMP), 2), -- 2 Bữa sáng P.401  = 200.000đ
  (6, 2406, 7, 1, 100000, DATEADD('DAY',  -2,  CURRENT_TIMESTAMP), 2); -- 1 Đền bù vỡ ly    = 100.000đ (Tổng BK-2406 = 300.000đ)

ALTER TABLE service_orders ALTER COLUMN order_id RESTART WITH 50;

-- 8. INVOICES (Hóa đơn lịch sử của BK-2406: Phòng 3.700k + DV 300k = Subtotal 4.000k; SVC 5% = 200k; VAT 10% = 420k; Grand Total = 4.620k)
INSERT INTO invoices (
  invoice_id, booking_id, room_charge, service_charge_total, early_checkin_fee, late_checkout_fee,
  subtotal, service_charge_amount, vat_amount, grand_total, deposit_deducted, balance_due,
  payment_method, paid_at, created_by
) VALUES
  (1, 2406, 3700000, 300000, 0, 0,
   4000000, 200000, 420000, 4620000, 1282050, 3337950,
   'TRANSFER', DATEADD('DAY', -2, CURRENT_TIMESTAMP), 2);

ALTER TABLE invoices ALTER COLUMN invoice_id RESTART WITH 10;
