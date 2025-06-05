package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.LineStop;
import ikw.school.busreservation.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface LineStopRepository extends JpaRepository<LineStop, Integer> {

    // 기존: 노선 ID로 정류장 목록 조회
    List<LineStop> findByLineOrderByStopOrderAsc(Line line);

    // ✅ 수정: 정확한 필드명은 'departureTime'
    @Query("SELECT s.arriveTime FROM Stop s " +
            "WHERE s.name = :name AND s.departureTime.id IN " +
            "(SELECT d.id FROM DepartureTime d WHERE d.line.id = :lineId)")
    List<LocalTime> findArriveTimesByStopNameAndLineId(@Param("name") String name, @Param("lineId") Integer lineId);
}
