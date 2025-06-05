package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.BusReservation;
import ikw.school.busreservation.repository.BusReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusNotificationSchedulerService {

    private final NotificationService notificationService;
    private final MemberService memberService;
    private final QRLogService qrLogService;
    private final BusReservationRepository busReservationRepository;

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void checkAndNotifyPenalties() {
        List<BusReservation> reservations = busReservationRepository.findAll();

        for (BusReservation reservation : reservations) {
            Timestamp departureTimestamp = reservation.getDepartureTime().getDeparture();
            if (departureTimestamp == null) continue;

            LocalDateTime departureTime = departureTimestamp.toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            // 아직 시간이 지나지 않았다면 무시
            if (departureTime.plusMinutes(5).isAfter(now)) {
                continue;
            }

            String userId = reservation.getUserId();
            Long reservationId = reservation.getId().longValue();

            boolean hasScanned = qrLogService.hasScannedQr(userId, reservationId);
            if (!hasScanned) {
                log.info("[페널티 발생] userId={}, reservationId={}, 출발시간={}", userId, reservationId, departureTime);

                // ✅ 페널티 알림 전송
                notificationService.sendPenaltyNotification(userId, "예약한 버스에 QR을 찍지 않아 페널티가 부여되었습니다.");

                // ✅ 페널티 카운트 증가
                memberService.addPenaltyToMember(userId);
            }
        }
    }
}
