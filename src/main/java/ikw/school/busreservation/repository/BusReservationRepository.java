package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.BusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BusReservationRepository extends JpaRepository<BusReservation, Integer> {

    List<BusReservation> findByUserId(String userId);

    List<BusReservation> findByDepartureTimeId(Integer departureTimeId);

    boolean existsByDepartureTimeIdAndSeatIdAndReservationStatus(Integer departureTimeId, Integer seatId, String reservationStatus);

    @Query("SELECT r.seat.id FROM BusReservation r WHERE r.departureTime.id = :departureTimeId AND r.reservationStatus = '예약완료'")
    List<Integer> findReservedSeatIdsByDepartureTimeId(@Param("departureTimeId") Integer departureTimeId);

    @Query("SELECT r.seat.seatNumber FROM BusReservation r WHERE r.departureTime.id = :departureTimeId AND r.reservationStatus = '예약완료'")
    List<Integer> findReservedSeatNumbersByDepartureTimeId(@Param("departureTimeId") Integer departureTimeId);

    // ✅ departure가 LocalDateTime일 때 예약완료 알림용 조회
    @Query("SELECT r FROM BusReservation r " +
            "JOIN r.departureTime d " +
            "WHERE r.reservationStatus = '예약완료' AND d.departure = :targetDateTime")
    List<BusReservation> findReservationsByDepartureDateTime(@Param("targetDateTime") LocalDateTime targetDateTime);

    int countReservedSeatsByDepartureTimeId(Integer departureTimeId);
}
