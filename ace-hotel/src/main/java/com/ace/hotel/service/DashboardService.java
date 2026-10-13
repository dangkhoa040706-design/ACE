package com.ace.hotel.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ace.hotel.dto.ActionLink;
import com.ace.hotel.dto.DashboardView;
import com.ace.hotel.dto.DisplayState;
import com.ace.hotel.dto.RoomCard;
import com.ace.hotel.dto.TodayRow;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.BookingStatus;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.RoomStatus;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.ServiceOrderRepository;

/**
 * Tính dữ liệu cho dashboard (plan 03-dashboard.md):
 * bốn ô KPI, thẻ phòng (trạng thái suy ra) và bảng check-in / check-out hôm nay.
 */
@Service
public class DashboardService {

    private static final List<BookingStatus> CHECK_IN_STATUSES =
            List.of(BookingStatus.BOOKED, BookingStatus.CHECKED_IN);
    private static final List<BookingStatus> CHECK_OUT_STATUSES =
            List.of(BookingStatus.CHECKED_IN, BookingStatus.CHECKED_OUT);

    private static final DateTimeFormatter DAY_MONTH = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter DAY_MONTH_TIME = DateTimeFormatter.ofPattern("dd/MM · HH:mm");
    private static final int MAX_CHIPS = 3;

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public DashboardService(RoomRepository roomRepository,
                            BookingRepository bookingRepository,
                            ServiceOrderRepository serviceOrderRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public DashboardView build() {
        LocalDate today = LocalDate.now();

        long freeRooms = roomRepository.countByStatus(RoomStatus.AVAILABLE);
        long checkInToday = bookingRepository.countByCheckInDateAndStatusIn(today, CHECK_IN_STATUSES);
        long checkOutToday = bookingRepository.countByCheckOutDateAndStatusIn(today, CHECK_OUT_STATUSES);
        long servicesToday = serviceOrderRepository.countByOrderedAtGreaterThanEqualAndOrderedAtLessThan(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        return new DashboardView(freeRooms, checkInToday, checkOutToday, servicesToday,
                buildRoomCards(today), buildTodayRows(today));
    }

    // ---------------------------------------------------------------- thẻ phòng

    private List<RoomCard> buildRoomCards(LocalDate today) {
        List<Room> rooms = roomRepository.findAllWithType();

        // phòng -> đặt phòng đang ở
        Map<Long, Booking> inHouse = new HashMap<>();
        for (Booking b : bookingRepository.findByStatusWithDetails(BookingStatus.CHECKED_IN)) {
            inHouse.put(b.getRoom().getId(), b);
        }
        // phòng -> đặt phòng nhận hôm nay
        Map<Long, Booking> arriving = new HashMap<>();
        for (Booking b : bookingRepository.findByStatusAndCheckInDateWithDetails(BookingStatus.BOOKED, today)) {
            arriving.putIfAbsent(b.getRoom().getId(), b);
        }
        Map<Long, List<String>> chips = serviceChips(inHouse.values());

        List<RoomCard> cards = new ArrayList<>();
        for (Room room : rooms) {
            Long roomId = room.getId();
            DisplayState state;
            Booking ref = null;
            if (room.getStatus() == RoomStatus.MAINTENANCE) {
                state = DisplayState.MAINTENANCE;
            } else if (inHouse.containsKey(roomId)) {
                ref = inHouse.get(roomId);
                state = ref.getCheckOutDate().isBefore(today) ? DisplayState.OVERDUE : DisplayState.OCCUPIED;
            } else if (arriving.containsKey(roomId)) {
                ref = arriving.get(roomId);
                state = DisplayState.RESERVED;
            } else {
                state = DisplayState.AVAILABLE;
            }

            List<ActionLink> actions = new ArrayList<>();
            switch (state) {
                case AVAILABLE -> actions.add(new ActionLink("Đặt phòng", "/bookings/search", true));
                case RESERVED -> actions.add(new ActionLink("Xem đặt phòng", "/bookings/" + ref.getId(), false));
                case OCCUPIED -> {
                    actions.add(new ActionLink("Check-out", "/bookings/" + ref.getId() + "/check-out", true));
                    actions.add(new ActionLink("Dịch vụ", "/bookings/" + ref.getId() + "/services", false));
                }
                case OVERDUE -> actions.add(new ActionLink("Check-out", "/bookings/" + ref.getId() + "/check-out", true));
                default -> {
                    // Bảo trì: không có thao tác nghiệp vụ (quản lý sửa trạng thái ở màn hình phòng)
                }
            }

            List<String> roomChips = ref != null && state != DisplayState.RESERVED
                    ? chips.getOrDefault(ref.getId(), List.of())
                    : List.of();
            cards.add(new RoomCard(
                    room.getRoomNumber(),
                    room.getRoomType().getName(),
                    room.getRoomType().getCapacity(),
                    room.getRoomType().getPricePerNight(),
                    state, roomChips, actions));
        }
        return cards;
    }

    /** Bookings đang ở -> tối đa 3 tên dịch vụ đã dùng, dạng "+ Ăn sáng". */
    private Map<Long, List<String>> serviceChips(java.util.Collection<Booking> inHouse) {
        Map<Long, List<String>> result = new HashMap<>();
        if (inHouse.isEmpty()) {
            return result;
        }
        List<Long> ids = inHouse.stream().map(Booking::getId).toList();
        Map<Long, Set<String>> names = new HashMap<>();
        for (ServiceOrder order : serviceOrderRepository.findByBookingIds(ids)) {
            names.computeIfAbsent(order.getBooking().getId(), k -> new LinkedHashSet<>())
                    .add(order.getService().getName());
        }
        names.forEach((bookingId, set) -> result.put(bookingId,
                set.stream().limit(MAX_CHIPS).map(n -> "+ " + n).toList()));
        return result;
    }

    // ---------------------------------------------------- bảng hôm nay

    private List<TodayRow> buildTodayRows(LocalDate today) {
        List<TodayRow> rows = new ArrayList<>();
        for (Booking b : bookingRepository.findTodayRows(today, BookingStatus.CANCELLED, BookingStatus.CHECKED_IN)) {
            DisplayState state = switch (b.getStatus()) {
                case BOOKED -> DisplayState.RESERVED;
                case CHECKED_IN -> b.getCheckOutDate().isBefore(today) ? DisplayState.OVERDUE : DisplayState.OCCUPIED;
                case CHECKED_OUT -> DisplayState.DONE;
                case CANCELLED -> throw new IllegalStateException("Đặt phòng đã hủy không hiển thị ở bảng hôm nay");
            };

            List<ActionLink> actions = new ArrayList<>();
            switch (b.getStatus()) {
                case BOOKED -> actions.add(new ActionLink("Check-in", "/bookings/" + b.getId() + "/check-in", true));
                case CHECKED_IN -> {
                    boolean due = !b.getCheckOutDate().isAfter(today);
                    if (due) {
                        actions.add(new ActionLink("Check-out", "/bookings/" + b.getId() + "/check-out", true));
                    } else {
                        actions.add(new ActionLink("Xem", "/bookings/" + b.getId(), false));
                    }
                }
                case CHECKED_OUT -> actions.add(new ActionLink("Xem hóa đơn", "/bookings/" + b.getId() + "#invoice", false));
                case CANCELLED -> {
                    // đã loại ở truy vấn
                }
            }

            String checkIn = b.getActualCheckIn() != null
                    ? b.getActualCheckIn().format(DAY_MONTH_TIME)
                    : b.getCheckInDate().format(DAY_MONTH);
            String checkOut = b.getActualCheckOut() != null
                    ? b.getActualCheckOut().format(DAY_MONTH_TIME)
                    : b.getCheckOutDate().format(DAY_MONTH);
            boolean highlight = (b.getStatus() == BookingStatus.BOOKED && b.getCheckInDate().equals(today))
                    || state == DisplayState.OVERDUE;

            rows.add(new TodayRow(
                    String.format("BK-%04d", b.getId()),
                    b.getCustomer().getFullName(),
                    b.getRoom().getRoomNumber() + " · " + b.getRoom().getRoomType().getName(),
                    checkIn, checkOut, state, highlight, actions));
        }
        return rows;
    }
}
