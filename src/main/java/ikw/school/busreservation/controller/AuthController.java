package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        ResponseEntity<String> response = authService.register(member);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", response.getBody());
            return "redirect:/join";
        }
    }

    // ✅ 실시간 중복 아이디 검사용 API
    @GetMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String userId) {
        boolean exists = authService.isUserIdDuplicate(userId);
        return ResponseEntity.ok(exists); // true면 중복됨
    }
}
