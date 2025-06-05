package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JoinController {

    // 회원가입 페이지 렌더링
    @GetMapping("/join")
    public String showJoinPage(Model model) {
        model.addAttribute("member", new Member());
        return "join"; // templates/join.html 로 연결
    }
}
