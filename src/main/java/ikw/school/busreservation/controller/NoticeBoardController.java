package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.entity.NoticeBoard;
import ikw.school.busreservation.service.MemberService;
import ikw.school.busreservation.service.NoticeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;
    private final MemberService memberService;

    public NoticeBoardController(NoticeBoardService noticeBoardService, MemberService memberService) {
        this.noticeBoardService = noticeBoardService;
        this.memberService = memberService;
    }

    // ✅ 공지사항 목록 페이지
    @GetMapping
    public String noticeList(Model model) {
        List<NoticeBoard> notices = noticeBoardService.findAll();
        model.addAttribute("notices", notices);
        return "notice";  // → templates/notice.html
    }

    // ✅ 공지사항 상세 페이지
    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable Integer id, Model model) {
        NoticeBoard notice = noticeBoardService.findById(id);
        if (notice == null) {
            return "redirect:/notice";
        }
        model.addAttribute("notice", notice);
        return "notice-detail";  // → templates/notice-detail.html
    }

    // ✅ 공지사항 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("notice", new NoticeBoard());
        return "notice-write";  // → templates/notice-write.html
    }

    // ✅ 공지사항 작성 처리 (작성자 = 로그인 사용자 이름)
    @PostMapping("/write")
    public String submitNotice(@ModelAttribute NoticeBoard noticeBoard, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            Member member = memberService.findByUserId(userId);
            if (member != null) {
                noticeBoard.setWriter(member.getName()); // 이름으로 작성자 설정
            } else {
                noticeBoard.setWriter("알 수 없음");
            }
        }

        noticeBoardService.save(noticeBoard);
        return "redirect:/notice";
    }

    // ✅ 공지사항 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        NoticeBoard notice = noticeBoardService.findById(id);
        if (notice == null) {
            return "redirect:/notice";
        }
        model.addAttribute("notice", notice);
        return "notice-edit";  // → templates/notice-edit.html
    }

    // ✅ 공지사항 수정 처리 (작성자 = 로그인 사용자 이름)
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable Integer id,
                               @ModelAttribute NoticeBoard updatedNotice,
                               HttpSession session) {
        NoticeBoard existing = noticeBoardService.findById(id);
        if (existing == null) {
            return "redirect:/notice";
        }

        String userId = (String) session.getAttribute("userId");
        String name = "알 수 없음";
        if (userId != null) {
            Member member = memberService.findByUserId(userId);
            if (member != null) {
                name = member.getName();
            }
        }

        existing.setTitle(updatedNotice.getTitle());
        existing.setContent(updatedNotice.getContent());
        existing.setWriter(name);

        noticeBoardService.save(existing);
        return "redirect:/notice/" + id;
    }

    // ✅ 공지사항 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Integer id) {
        noticeBoardService.delete(id);
        return "redirect:/notice";
    }
}
