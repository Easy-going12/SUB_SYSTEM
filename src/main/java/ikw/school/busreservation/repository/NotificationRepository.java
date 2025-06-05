package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 사용자 ID 기준 알림 내림차순 조회
    List<Notification> findByUserIdOrderBySentAtDesc(String userId);

    // 사용자 + 카테고리 기준 알림 내림차순 조회
    List<Notification> findByUserIdAndCategoryOrderBySentAtDesc(String userId, String category);

    // 중복 알림 방지를 위한 존재 여부 확인
    boolean existsByUserIdAndContentAndCategory(String userId, String content, String category);
}
