package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.DepartureTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartureTimeRepository extends JpaRepository<DepartureTime, Integer> {

    // ✅ 특정 노선 + 등교/하교 타입 기준으로 출발시간 정렬 조회
    List<DepartureTime> findByLineIdAndReservationTypeOrderByDepartureAsc(Integer lineId, String reservationType);
}
