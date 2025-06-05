package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.MessageBox;
import ikw.school.busreservation.service.MessageBoxService;
import ikw.school.busreservation.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/messagebox")
public class MessageBoxController {

    private final MessageBoxService messageBoxService;
    private final NotificationService notificationService;

    public MessageBoxController(MessageBoxService messageBoxService,
                                NotificationService notificationService) {
        this.messageBoxService = messageBoxService;
        this.notificationService = notificationService;
    }

    // ✅ 받은 쪽지함 목록
    @GetMapping
    public String inbox(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        List<MessageBox> messages = messageBoxService.getReceivedMessages(userId);
        model.addAttribute("messages", messages);
        return "messagebox";
    }

    // ✅ 쪽지 작성 폼
    @GetMapping("/send")
    public String writeForm(Model model) {
        model.addAttribute("message", new MessageBox());
        return "message-write";
    }

    // ✅ 쪽지 전송 처리 + 알림 전송 추가
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute MessageBox message,
                              HttpSession session) {
        String senderId = (String) session.getAttribute("userId");
        message.setSenderId(senderId);

        // ✅ 쪽지 저장
        messageBoxService.sendMessage(message);

        // ✅ 쪽지 알림 전송
        notificationService.sendMessageNotification(
                senderId,
                message.getReceiverId(),
                message.getTitle()
        );

        return "redirect:/messagebox";
    }

    // ✅ 쪽지 상세 보기
    @GetMapping("/view/{id}")
    public String viewMessage(@PathVariable Integer id,
                              Model model) {
        MessageBox message = messageBoxService.getMessageById(id);
        if (message != null) {
            messageBoxService.markAsRead(id);
            model.addAttribute("message", message);
            return "message-detail";
        } else {
            return "redirect:/messagebox";
        }
    }

    // ✅ 쪽지 삭제
    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Integer id) {
        messageBoxService.deleteMessage(id);
        return "redirect:/messagebox";
    }
}
