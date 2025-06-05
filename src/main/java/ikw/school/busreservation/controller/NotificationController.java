package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Notification;
import ikw.school.busreservation.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // 전체 알림 조회
    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable String userId) {
        return service.getUserNotifications(userId);
    }

    // 카테고리별 알림 조회
    @GetMapping("/{userId}/category/{category}")
    public List<Notification> getNotificationsByCategory(@PathVariable String userId, @PathVariable String category) {
        return service.getUserNotificationsByCategory(userId, category);
    }

    // 알림 생성
    @PostMapping
    public void sendNotification(@RequestParam String userId, @RequestParam String message, @RequestParam String category) {
        service.sendNotification(userId, message, category);
    }

    // 쪽지용 알림 (내부적으로 자동 호출)
    @PostMapping("/message")
    public void sendMessageNotification(@RequestParam String senderId, @RequestParam String receiverId, @RequestParam String title) {
        service.sendMessageNotification(senderId, receiverId, title);
    }

    // 알림 확인 처리
    @PatchMapping("/check/{notificationId}")
    public void checkNotification(@PathVariable Long notificationId) {
        service.markAsChecked(notificationId);
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        service.deleteNotification(notificationId);
    }
}
