package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.BusReservation;
import ikw.school.busreservation.service.HistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bus")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    // ✅ 예약 내역 페이지
    @GetMapping("/log")
    public String showReservationLog(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        List<BusReservation> reservations = historyService.getReservationsByUserId(userId);
        model.addAttribute("reservations", reservations);
        return "reservation-history";
    }

    // ✅ 예약 취소 처리
    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam("id") Integer reservationId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        historyService.deleteReservation(reservationId); // 물리 삭제로 변경
        return "redirect:/bus/log";
    }
}
