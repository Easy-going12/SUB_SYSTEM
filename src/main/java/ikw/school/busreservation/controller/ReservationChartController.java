package ikw.school.busreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReservationChartController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ 2. 노선 기반 예약 평균 차트 (lineId로 조회)
    @GetMapping("/reservations/chart/line")
    public List<Map<String, Object>> getAverageReservationsByLine(
            @RequestParam Long lineId
    ) {
        String sql = """
            SELECT 
                DATE_FORMAT(d.departure, '%H:00') AS time,
                ROUND(AVG(CASE WHEN br.reservation_status = 'RESERVED' THEN 1 ELSE 0 END), 1) AS avg_reserved_count
            FROM BusReservation br
            JOIN DepartureTime d ON br.departure_time_id = d.id
            WHERE d.line_id = ?
              AND br.reserved_at >= CURDATE() - INTERVAL 2 DAY
            GROUP BY time
            ORDER BY time
        """;

        return jdbcTemplate.queryForList(sql, lineId);
    }
}
