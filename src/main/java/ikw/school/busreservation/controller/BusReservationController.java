package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.*;
import ikw.school.busreservation.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class BusReservationController {

    private final LineService lineService;
    private final DepartureTimeService departureTimeService;
    private final StopService stopService;
    private final SeatService seatService;
    private final BusReservationService busReservationService;

    public BusReservationController(LineService lineService,
                                    DepartureTimeService departureTimeService,
                                    StopService stopService,
                                    SeatService seatService,
                                    BusReservationService busReservationService) {
        this.lineService = lineService;
        this.departureTimeService = departureTimeService;
        this.stopService = stopService;
        this.seatService = seatService;
        this.busReservationService = busReservationService;
    }

    // 예약 페이지 GET
    @GetMapping
    public String showReservationPage(Model model) {
        model.addAttribute("lines", lineService.getAllLines());
        return "reservation"; // templates/reservation.html
    }

    // 예약 처리 POST
    @PostMapping("/submit")
    public String submitReservation(@RequestParam String reservationType,
                                    @RequestParam Integer lineId,
                                    @RequestParam Integer departureTimeId,
                                    @RequestParam Integer stopId,
                                    @RequestParam Integer seatNumber,
                                    HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Line line = lineService.getLineById(lineId);
        DepartureTime departureTime = departureTimeService.getById(departureTimeId);
        Stop stop = stopService.getStopById(stopId);

        Integer busId = departureTime.getBus().getId();
        Seat seat = seatService.getSeatByBusAndNumber(busId, seatNumber);

        BusReservation reservation = BusReservation.builder()
                .userId(userId)
                .reservationType(reservationType)
                .line(line)
                .departureTime(departureTime)
                .stop(stop)
                .seat(seat)
                .reservationStatus("예약완료")
                .reservedAt(Timestamp.from(Instant.now()))
                .build();

        busReservationService.saveReservation(reservation);
        return "redirect:/reservation/history";
    }

    // 🔄 출발 시간 조회 API (Line + Type 기준)
    @GetMapping("/departures")
    @ResponseBody
    public List<DepartureTime> getDepartureTimesByLineAndType(@RequestParam Integer lineId,
                                                              @RequestParam String type) {
        return departureTimeService.getDepartureTimesByLineAndType(lineId, type);
    }

    // 정류장 목록 조회 (노선 + 출발시간 기준)
    @GetMapping("/stations")
    @ResponseBody
    public List<Stop> getStopsByLineAndDeparture(@RequestParam("lineId") Integer lineId,
                                                 @RequestParam("departureTimeId") Integer departureTimeId) {
        return stopService.getStopsByLineAndDeparture(lineId, departureTimeId);
    }

    // ✅ 선택된 출발시간과 버스 ID에 해당하는 예약된 좌석 번호 목록 반환
    @GetMapping("/seats")
    @ResponseBody
    public List<Integer> getReservedSeatNumbers(@RequestParam Integer departureTimeId,
                                                @RequestParam Integer busId) {
        return busReservationService.getReservedSeatNumbersByBus(departureTimeId, busId);
    }

    @GetMapping("/history")
    public String viewReservationHistory(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        List<BusReservation> reservations = busReservationService.findByUserId(userId);
        model.addAttribute("reservations", reservations);
        return "reservation-history"; // templates/reservation-history.html
    }

}
