package com.ace.hotel.exception;

public final class MessageConstants {

    private MessageConstants() {}

    // UC01: Đăng ký, Đăng nhập & Phân quyền 3 Role
    public static final String MSG_01 = "Sai tên đăng nhập hoặc mật khẩu.";
    public static final String MSG_02 = "Bạn đã đăng xuất khỏi hệ thống ACE Hotel.";
    public static final String MSG_03 = "Đăng ký tài khoản khách hàng thành công! Chào mừng bạn đến với ACE Hotel.";
    public static final String MSG_04 = "Tên đăng nhập này đã được sử dụng. Vui lòng chọn tên đăng nhập khác.";
    public static final String MSG_05 = "Bạn không có quyền truy cập vào phân hệ chức năng này.";

    // UC02: Quản lý Danh mục Phòng, Biểu giá & Dịch vụ
    public static final String MSG_06 = "Lưu thông tin hạng phòng và biểu giá thành công.";
    public static final String MSG_07 = "Đã xóa hạng phòng thành công.";
    public static final String MSG_08 = "Lưu thông tin phòng vật lý thành công.";
    public static final String MSG_09 = "Đã xóa phòng vật lý thành công.";
    public static final String MSG_10 = "Lưu danh mục dịch vụ thành công.";
    public static final String MSG_11 = "Đã xóa dịch vụ khỏi danh mục.";
    public static final String MSG_12 = "Số phòng hoặc tên danh mục đã tồn tại. Vui lòng kiểm tra lại.";
    public static final String MSG_13 = "Đơn giá ngày thường, giá cuối tuần phải lớn hơn 0 VNĐ và sức chứa tối thiểu là 1 khách.";
    public static final String MSG_14 = "Không thể xóa hạng phòng này vì đang có phòng vật lý hoặc đơn đặt phòng tham chiếu.";
    public static final String MSG_15 = "Không thể xóa phòng đã có lịch sử đặt phòng. Vui lòng chuyển phòng sang trạng thái Bảo trì nếu ngưng khai thác.";
    public static final String MSG_16 = "Không thể chuyển sang trạng thái Bảo trì vì phòng đang có khách lưu trú (OCCUPIED).";
    public static final String MSG_17 = "Không thể xóa dịch vụ này vì đã phát sinh trong lịch sử Room Folio của khách hàng.";

    // UC03: Tìm kiếm & Đặt phòng trực tuyến (Quota, Cọc 30%, QR)
    public static final String MSG_18 = "Đặt phòng và thanh toán cọc 30%% thành công! Mã đặt phòng của quý khách là %s.";
    public static final String MSG_19 = "Đã hủy đơn đặt phòng giữ chỗ %s thành công.";
    public static final String MSG_20 = "Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 đêm.";
    public static final String MSG_21 = "Không còn hạng phòng trống phù hợp trong khoảng ngày và số lượng khách đã chọn.";
    public static final String MSG_22 = "Hạng phòng này vừa hết phòng trống trong khoảng thời gian quý khách chọn. Vui lòng chọn hạng phòng khác hoặc thay đổi ngày lưu trú.";
    public static final String MSG_23 = "Chỉ có thể hủy đơn đặt phòng khi đang ở trạng thái Đã xác nhận giữ chỗ (RESERVED).";

    // UC04: Quản lý Check-in & Tiếp nhận khách vãng lai
    public static final String MSG_24 = "Nhận phòng thành công. Đã bàn giao Phòng %s cho đơn đặt %s.";
    public static final String MSG_25 = "Tiếp nhận khách vãng lai thành công. Đã bàn giao Phòng %s.";
    public static final String MSG_26 = "Không tìm thấy mã đặt phòng %s hoặc đơn không ở trạng thái chờ nhận phòng.";
    public static final String MSG_27 = "Hiện chưa có phòng vật lý nào thuộc hạng phòng này ở trạng thái sẵn sàng. Vui lòng xác nhận dọn sạch phòng trước khi bàn giao.";
    public static final String MSG_28 = "Phòng %s vừa được bàn giao cho khách khác hoặc chưa sẵn sàng đón khách.";

    // UC05: Check-out & Quyết toán
    public static final String MSG_29 = "Quyết toán trả phòng thành công. Đã xuất Hóa đơn #%s cho Phòng %s.";
    public static final String MSG_30 = "Đã hoàn tất dọn phòng. Phòng %s đã sẵn sàng đón khách mới.";
    public static final String MSG_31 = "Chỉ có thể thực hiện quyết toán trả phòng cho đơn đang lưu trú.";

    // UC06: Ghi nhận dịch vụ lưu trú vào Room Folio
    public static final String MSG_32 = "Đã ghi nhận dịch vụ vào Room Folio của Phòng %s thành công.";
    public static final String MSG_33 = "Đã xóa dòng dịch vụ khỏi Room Folio thành công.";
    public static final String MSG_34 = "Chỉ được phép thêm hoặc xóa dịch vụ trong Room Folio khi phòng đang có khách lưu trú (CHECKED_IN).";
    public static final String MSG_35 = "Số lượng dịch vụ sử dụng phải lớn hơn hoặc bằng 1.";
}
