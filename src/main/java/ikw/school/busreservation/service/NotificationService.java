package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Notification;
import ikw.school.busreservation.entity.BusReservation;
import ikw.school.busreservation.repository.NotificationRepository;
import ikw.school.busreservation.repository.BusReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BusReservationRepository busReservationRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               BusReservationRepository busReservationRepository) {
        this.notificationRepository = notificationRepository;
        this.busReservationRepository = busReservationRepository;
    }

    // ✅ 공통 알림 전송
    public void sendNotification(String userId, String message, String category) {
        Notification noti = new Notification();
        noti.setUserId(userId);
        noti.setContent(message);
        noti.setSentAt(LocalDateTime.now());
        noti.setChecked(false);
        noti.setCategory(category);
        notificationRepository.save(noti);
    }

    // ✅ 쪽지 알림 전송
    public void sendMessageNotification(String senderId, String receiverId, String title) {
        String message = String.format("%s님이 '%s' 제목의 쪽지를 보냈습니다.", senderId, title);
        sendNotification(receiverId, message, "message");
    }

    // ✅ 사용자 전체 알림 조회
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderBySentAtDesc(userId);
    }

    // ✅ 카테고리별 알림 조회
    public List<Notification> getUserNotificationsByCategory(String userId, String category) {
        return notificationRepository.findByUserIdAndCategoryOrderBySentAtDesc(userId, category);
    }

    // ✅ 알림 읽음 처리
    public void markAsChecked(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        optional.ifPresent(noti -> {
            noti.setChecked(true);
            notificationRepository.save(noti);
        });
    }

    // ✅ 알림 삭제
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // ✅ 버스 출발 시간 알림
    public void checkAndSendTimeAlerts() {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> alertMinutes = List.of(10, 20, 30);

        for (int minute : alertMinutes) {
            LocalDateTime targetTime = now.plusMinutes(minute).withSecond(0).withNano(0);

            List<BusReservation> reservations =
                    busReservationRepository.findReservationsByDepartureDateTime(targetTime);

            for (BusReservation reservation : reservations) {
                String userId = reservation.getUserId();
                String content = String.format("🚌 버스 출발 %d분 전입니다. 탑승을 준비해주세요.", minute);

                boolean exists = notificationRepository.existsByUserIdAndContentAndCategory(
                        userId, content, "bus");

                if (!exists) {
                    Notification notification = new Notification();
                    notification.setUserId(userId);
                    notification.setContent(content);
                    notification.setCategory("bus");
                    notification.setSentAt(LocalDateTime.now());
                    notification.setChecked(false);
                    notificationRepository.save(notification);
                }
            }
        }
    }

    // ✅ 페널티 알림 전송 (userId 기반)
    public void sendPenaltyNotification(String userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setCategory("penalty");
        notification.setContent(message);
        notification.setChecked(false);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
