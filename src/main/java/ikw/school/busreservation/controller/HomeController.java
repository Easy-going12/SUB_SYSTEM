package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.entity.MessageBox;
import ikw.school.busreservation.entity.NoticeBoard;
import ikw.school.busreservation.service.MemberService;
import ikw.school.busreservation.service.MessageBoxService;
import ikw.school.busreservation.service.NoticeBoardService;
import ikw.school.busreservation.service.QrService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final NoticeBoardService noticeBoardService;
    private final MessageBoxService messageBoxService;
    private final MemberService memberService;

    public HomeController(NoticeBoardService noticeBoardService, MessageBoxService messageBoxService, MemberService memberService) {
        this.noticeBoardService = noticeBoardService;
        this.messageBoxService = messageBoxService;
        this.memberService = memberService;
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("userId", userId);

        // 이름 표시용
        Member member = memberService.findByUserId(userId);
        if (member != null) {
            model.addAttribute("name", member.getName());
        }

        // 최근 공지사항 3개
        List<NoticeBoard> recentNotices = noticeBoardService.findRecentNotices(3);
        model.addAttribute("recentNotices", recentNotices);

        // 최근 쪽지 3개
        List<MessageBox> recentMessages = messageBoxService.getReceivedMessages(userId)
                .stream()
                .limit(3)
                .toList();
        model.addAttribute("recentMessages", recentMessages);

        // ⚡ QR 코드 생성
        try {
            // 예시 데이터 — 실제로는 예약 정보나 DB에서 조회해야 함
            String busId = "B123";
            String lineId = "L456";
            String departure = "2025-04-24T08:00";  // ISO 형식의 출발 시간 예시

            String qrContent = String.format(
                    "user_id=%s&bus_id=%s&line_id=%s&departure=%s",
                    userId, busId, lineId, departure
            );

            String qrCodeBase64 = QrService.generateQrCodeImage(qrContent);
            model.addAttribute("qrCode", qrCodeBase64);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("qrCode", null);
        }

        return "home";
    }
}
