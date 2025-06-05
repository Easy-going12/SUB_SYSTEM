package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.BusReservation;
import ikw.school.busreservation.repository.BusReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoryService {

    private final BusReservationRepository reservationRepository;

    public HistoryService(BusReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // ✅ 유저의 예약 내역 조회
    public List<BusReservation> getReservationsByUserId(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    // ✅ 예약 취소 처리 (물리 삭제)
    @Transactional
    public void deleteReservation(Integer reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new IllegalArgumentException("해당 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(reservationId);
    }
}