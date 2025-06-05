package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StopRepository extends JpaRepository<Stop, Integer> {

    // 1. 출발시간 ID 기준 정류장 정렬 조회
    List<Stop> findByDepartureTimeIdOrderByArriveTimeAsc(Integer departureTimeId);

    // 2. 노선 ID + 출발시간 ID 기준 정류장 조회
    @Query("SELECT s FROM Stop s " +
            "WHERE s.departureTime.id = :departureTimeId " +
            "AND s.departureTime.line.id = :lineId " +
            "ORDER BY s.arriveTime ASC")
    List<Stop> findStopsByLineAndDeparture(@Param("lineId") Integer lineId,
                                           @Param("departureTimeId") Integer departureTimeId);
}
