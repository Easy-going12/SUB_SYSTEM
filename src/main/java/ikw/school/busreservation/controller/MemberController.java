package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // ✅ 마이페이지 - 회원정보 조회
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Member member = memberService.findByUserId(userId);
        if (member == null) {
            model.addAttribute("errorMessage", "사용자 정보를 불러올 수 없습니다.");
            return "mypage";
        }

        model.addAttribute("member", member);
        return "mypage";
    }

    // ✅ 연락처 수정 폼 페이지
    @GetMapping("/update-phone")
    public String updatePhoneForm(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Member member = memberService.findByUserId(userId);
        if (member == null) {
            model.addAttribute("errorMessage", "사용자 정보를 불러올 수 없습니다.");
            return "update-phone"; // 에러 메시지를 포함한 페이지 표시
        }

        model.addAttribute("member", member);
        return "update-phone"; // 연락처 수정용 HTML
    }

    // ✅ 연락처 변경 처리
    @PostMapping("/update-phone")
    public String updatePhone(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            memberService.updatePhoneNumber(userId, phoneNumber);
        }
        return "redirect:/member/mypage";
    }

    // ✅ 비밀번호 변경 폼
    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "change-password";
    }

    // ✅ 비밀번호 변경 처리 (성공 시 마이페이지로 이동)
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model) {
        String userId = (String) session.getAttribute("userId");
        Member member = memberService.findByUserId(userId);

        if (member != null && member.getPassword().equals(currentPassword)) {
            member.setPassword(newPassword);
            memberService.save(member);
            return "redirect:/member/mypage";
        } else {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "change-password";
        }
    }

    // ✅ 회원탈퇴 확인 폼 (비밀번호 입력 화면)
    @GetMapping("/confirm-delete")
    public String confirmDeleteForm() {
        return "confirm-delete";
    }

    // ✅ 비밀번호 확인 후 JS confirm() 통해 최종 탈퇴 확인
    @PostMapping("/delete")
    public String deleteAccount(@RequestParam String password,
                                HttpSession session,
                                Model model) {
        String userId = (String) session.getAttribute("userId");
        Member member = memberService.findByUserId(userId);

        if (member != null && member.getPassword().equals(password)) {
            memberService.deleteByUserId(userId);
            session.invalidate();
            return "redirect:/login"; // 탈퇴 후 로그인 페이지로 이동
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "confirm-delete"; // 다시 입력
        }
    }

    // ✅ 실시간 아이디 중복 확인 API
    @GetMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String userId) {
        boolean exists = memberService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }
}