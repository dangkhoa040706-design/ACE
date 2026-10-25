package com.ace.hotel.service;

import com.ace.hotel.dto.AdminHomeDashboardDto;
import com.ace.hotel.dto.AdminReportDto;
import com.ace.hotel.entity.Invoice;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.PaymentMethod;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.InvoiceRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final RoomRepository roomRepository;
    private final InvoiceRepository invoiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final BookingRepository bookingRepository;

    public ReportService(RoomRepository roomRepository,
                          InvoiceRepository invoiceRepository,
                          ServiceOrderRepository serviceOrderRepository,
                          BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.invoiceRepository = invoiceRepository;
        this.serviceOrderRepository = serviceOrderRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Lấy dữ liệu tổng quan cho Trang Chủ Admin (Executive Dashboard 5★)
     */
    public AdminHomeDashboardDto getAdminHomeDashboardData() {
        AdminHomeDashboardDto dto = new AdminHomeDashboardDto();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // 1. Lời chào theo buổi & Lời chúc điều hành 5★
        int hour = now.getHour();
        if (hour >= 5 && hour < 12) {
            dto.setGreetingTitle("Chào buổi sáng, Quý Quản Trị Viên!");
            dto.setGreetingPeriod("BUỔI SÁNG");
            dto.setGreetingQuote("Chúc bạn một ngày mới tràn đầy năng lượng, tối ưu hóa công suất phòng và mang lại trải nghiệm đỉnh cao cho khách lưu trú.");
        } else if (hour >= 12 && hour < 14) {
            dto.setGreetingTitle("Chào buổi trưa, Quý Quản Trị Viên!");
            dto.setGreetingPeriod("BUỔI TRƯA");
            dto.setGreetingQuote("Nửa ngày làm việc hiệu quả đã trôi qua. Chúc bạn có giờ nghỉ trưa thư thái và những quyết định điều hành sáng suốt.");
        } else if (hour >= 14 && hour < 18) {
            dto.setGreetingTitle("Chào buổi chiều, Quý Quản Trị Viên!");
            dto.setGreetingPeriod("BUỔI CHIỀU");
            dto.setGreetingQuote("Khách sạn là nghệ thuật của sự chu đáo và tỉ mỉ. Chúc bạn một buổi chiều làm việc hanh thông và thuận lợi.");
        } else if (hour >= 18 && hour < 23) {
            dto.setGreetingTitle("Chào buổi tối, Quý Quản Trị Viên!");
            dto.setGreetingPeriod("BUỔI TỐI");
            dto.setGreetingQuote("Chúc khách sạn một buổi tối rực rỡ, sảnh đón tiếp chu đáo và tất cả quý khách đều có những giây phút nghỉ dưỡng hoàn hảo.");
        } else {
            dto.setGreetingTitle("Chúc Quý Quản Trị Viên một đêm an lành!");
            dto.setGreetingPeriod("ĐÊM KHUYA");
            dto.setGreetingQuote("Hệ thống bảo an và vận hành ban đêm đang hoạt động ổn định. Chúc bạn một giấc ngủ an yên và tĩnh tại.");
        }

        // 2. Thứ ngày tháng năm tiếng Việt
        String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
        String dayName = days[today.getDayOfWeek().getValue() % 7];
        dto.setCurrentDateText(String.format("%s, %02d/%02d/%d", dayName, today.getDayOfMonth(), today.getMonthValue(), today.getYear()));

        // 3. KPI Công suất phòng
        long totalRooms = roomRepository.count();
        long sellableRooms = roomRepository.countAllSellableRooms();
        long occupiedRooms = roomRepository.countByStatus(RoomStatus.OCCUPIED);
        long availableRooms = roomRepository.countByStatus(RoomStatus.AVAILABLE);
        long dirtyRooms = roomRepository.countByStatus(RoomStatus.DIRTY);
        long maintenanceRooms = roomRepository.countByStatus(RoomStatus.MAINTENANCE);

        double occupancyRate = sellableRooms > 0 ? ((double) occupiedRooms / sellableRooms) * 100.0 : 0.0;
        dto.setTotalRooms(totalRooms);
        dto.setSellableRooms(sellableRooms);
        dto.setOccupiedRooms(occupiedRooms);
        dto.setAvailableRooms(availableRooms);
        dto.setDirtyRooms(dirtyRooms);
        dto.setMaintenanceRooms(maintenanceRooms);
        dto.setOccupancyRate(Math.round(occupancyRate * 10.0) / 10.0);

        // 4. KPI Doanh thu hôm nay
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59, 59);
        List<Invoice> todayInvoices = invoiceRepository.findByPaidAtBetweenOrderByPaidAtDesc(todayStart, todayEnd);
        long todayRev = 0L;
        for (Invoice inv : todayInvoices) {
            if (inv.getGrandTotal() != null) {
                todayRev += inv.getGrandTotal();
            }
        }
        dto.setTodayRevenue(todayRev);
        dto.setTodayInvoicesCount(todayInvoices.size());

        // 5. KPI Doanh thu tháng này
        LocalDate monthStartDay = today.withDayOfMonth(1);
        LocalDateTime monthStart = monthStartDay.atStartOfDay();
        List<Invoice> monthInvoices = invoiceRepository.findByPaidAtBetweenOrderByPaidAtDesc(monthStart, todayEnd);
        long monthRev = 0L;
        for (Invoice inv : monthInvoices) {
            if (inv.getGrandTotal() != null) {
                monthRev += inv.getGrandTotal();
            }
        }
        dto.setMonthRevenue(monthRev);
        dto.setMonthInvoicesCount(monthInvoices.size());

        // 6. Lưu lượng khách hôm nay
        long arrivals = bookingRepository.countByCheckInDateAndStatus(today, BookingStatus.RESERVED);
        long departures = bookingRepository.countByCheckOutDateAndStatus(today, BookingStatus.CHECKED_IN);
        long inHouse = bookingRepository.countByStatus(BookingStatus.CHECKED_IN);
        dto.setArrivalsCount(arrivals);
        dto.setDeparturesCount(departures);
        dto.setInHouseCount(inHouse);

        // 7. Giám sát Top 5 Booking gần nhất
        dto.setRecentBookings(bookingRepository.findTop5ByOrderByCreatedAtDesc());

        // 8. Danh sách phòng bẩn cần giục dọn
        dto.setDirtyRoomsList(roomRepository.findByStatusOrderByRoomNumberAsc(RoomStatus.DIRTY));

        // 9. Toàn bộ 12 phòng xếp theo tầng & số phòng cho Ma trận buồng phòng (Swiss Grid)
        List<com.ace.hotel.entity.Room> allRooms = roomRepository.findAllByOrderByFloorAscRoomNumberAsc();
        dto.setAllRoomsList(allRooms);
        dto.setRoomsByFloor(allRooms.stream()
                .collect(Collectors.groupingBy(com.ace.hotel.entity.Room::getFloor, TreeMap::new, Collectors.toList())));

        return dto;
    }

    /**
     * UC07: Tạo Báo cáo Đa chiều Doanh thu ++ & Công suất phòng chuẩn quốc tế
     */
    public AdminReportDto generateReport(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            fromDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        AdminReportDto report = new AdminReportDto();
        report.setFromDate(fromDate);
        report.setToDate(toDate);

        // 1. Chỉ số Công suất phòng (Operational KPIs)
        long totalRooms = roomRepository.count();
        long maintenanceRooms = roomRepository.countByStatus(RoomStatus.MAINTENANCE);
        long sellableRooms = roomRepository.countAllSellableRooms(); // Loại trừ MAINTENANCE
        long occupiedRooms = roomRepository.countByStatus(RoomStatus.OCCUPIED);

        double occupancyRate = sellableRooms > 0 ? ((double) occupiedRooms / sellableRooms) * 100.0 : 0.0;

        report.setTotalRooms(totalRooms);
        report.setMaintenanceRooms(maintenanceRooms);
        report.setSellableRooms(sellableRooms);
        report.setOccupiedRooms(occupiedRooms);
        report.setOccupancyRate(Math.round(occupancyRate * 10.0) / 10.0); // Làm tròn 1 chữ số thập phân

        // 2. Truy vấn hóa đơn trong kỳ lọc [fromDate, toDate]
        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.atTime(23, 59, 59);
        List<Invoice> invoices = invoiceRepository.findByPaidAtBetweenOrderByPaidAtDesc(start, end);
        report.setInvoices(invoices);
        report.setTotalInvoicesCount(invoices.size());

        // 3. Cơ cấu Doanh thu ++
        long totalRoomRevenue = 0L;
        long totalServiceRevenue = 0L;
        long totalSurchargeRevenue = 0L;
        long totalSubtotal = 0L;
        long totalSvcRevenue = 0L;
        long totalVatRevenue = 0L;
        long totalGrandRevenue = 0L;
        long totalDepositDeducted = 0L;
        long totalBalanceDue = 0L;
        long totalNightsPaid = 0L;

        Map<String, Long> paymentTotals = new HashMap<>();
        paymentTotals.put(PaymentMethod.CASH.name(), 0L);
        paymentTotals.put(PaymentMethod.CARD.name(), 0L);
        paymentTotals.put(PaymentMethod.TRANSFER.name(), 0L);

        for (Invoice inv : invoices) {
            totalRoomRevenue += inv.getRoomCharge();
            totalServiceRevenue += inv.getServiceChargeTotal();
            totalSurchargeRevenue += (inv.getEarlyCheckinFee() + inv.getLateCheckoutFee());
            totalSubtotal += inv.getSubtotal();
            totalSvcRevenue += inv.getServiceChargeAmount();
            totalVatRevenue += inv.getVatAmount();
            totalGrandRevenue += inv.getGrandTotal();
            totalDepositDeducted += inv.getDepositDeducted();
            totalBalanceDue += inv.getBalanceDue();

            if (inv.getBooking() != null) {
                totalNightsPaid += inv.getBooking().getNights();
            }

            if (inv.getPaymentMethod() != null) {
                String pmName = inv.getPaymentMethod().name();
                paymentTotals.put(pmName, paymentTotals.getOrDefault(pmName, 0L) + inv.getGrandTotal());
            }
        }

        report.setTotalRoomRevenue(totalRoomRevenue);
        report.setTotalServiceRevenue(totalServiceRevenue);
        report.setTotalSurchargeRevenue(totalSurchargeRevenue);
        report.setTotalSubtotal(totalSubtotal);
        report.setTotalSvcRevenue(totalSvcRevenue);
        report.setTotalVatRevenue(totalVatRevenue);
        report.setTotalGrandRevenue(totalGrandRevenue);
        report.setTotalDepositDeducted(totalDepositDeducted);
        report.setTotalBalanceDue(totalBalanceDue);
        report.setTotalNightsPaid(totalNightsPaid);
        report.setPaymentMethodTotals(paymentTotals);

        // 4. ADR & RevPAR
        long adr = totalNightsPaid > 0 ? Math.round((double) totalRoomRevenue / totalNightsPaid) : 0L;
        long daysInPeriod = Math.max(1, ChronoUnit.DAYS.between(fromDate, toDate) + 1);
        long revPar = (sellableRooms * daysInPeriod) > 0 ?
                Math.round((double) totalRoomRevenue / (sellableRooms * daysInPeriod)) : 0L;

        report.setAdr(adr);
        report.setRevPar(revPar);

        // 5. Bóc tách doanh thu 4 nhóm dịch vụ: MINIBAR, LAUNDRY, FB, DAMAGES
        List<Long> bookingIds = invoices.stream()
                .filter(i -> i.getBooking() != null)
                .map(i -> i.getBooking().getBookingId())
                .toList();

        long minibar = 0L;
        long laundry = 0L;
        long fb = 0L;
        long damages = 0L;

        if (!bookingIds.isEmpty()) {
            List<ServiceOrder> orders = serviceOrderRepository.findByBooking_BookingIdIn(bookingIds);
            for (ServiceOrder o : orders) {
                if (o.getService() != null && o.getService().getCategory() != null) {
                    switch (o.getService().getCategory()) {
                        case MINIBAR -> minibar += o.getLineTotal();
                        case LAUNDRY -> laundry += o.getLineTotal();
                        case FB -> fb += o.getLineTotal();
                        case DAMAGES -> damages += o.getLineTotal();
                    }
                }
            }
        }

        report.setRevenueMinibar(minibar);
        report.setRevenueLaundry(laundry);
        report.setRevenueFb(fb);
        report.setRevenueDamages(damages);

        return report;
    }
}
