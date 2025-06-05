package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.QRLog;
import ikw.school.busreservation.service.QRLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QRLogController {

    private final QRLogService qrLogService;

    public QRLogController(QRLogService qrLogService) {
        this.qrLogService = qrLogService;
    }

    @GetMapping("/boarding")
    public String showBoardingHistory(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        List<QRLog> logs = qrLogService.getLogsForUser(userId);
        model.addAttribute("qrLogs", logs);
        return "boarding-history";
    }

    @PostMapping("/boarding/delete/{id}")
    public String deleteQrLog(@PathVariable Long id) {
        qrLogService.deleteLogById(id);
        return "redirect:/boarding"; // 삭제 후 탑승내역 페이지로 리디렉션
    }
}

