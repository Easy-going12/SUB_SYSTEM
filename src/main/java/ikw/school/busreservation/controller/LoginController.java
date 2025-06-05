package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.service.LoginService;
import ikw.school.busreservation.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    public LoginController(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    // 로그인 페이지 이동
    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (loginService.login(userId, password)) {
            session.setAttribute("userId", userId); // 세션에 ID 저장

            // ✅ 사용자 이름 조회해서 세션에 저장
            Member member = memberService.findByUserId(userId);
            if (member != null) {
                session.setAttribute("name", member.getName());
            }

            return "redirect:/home";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/login";
    }
}
