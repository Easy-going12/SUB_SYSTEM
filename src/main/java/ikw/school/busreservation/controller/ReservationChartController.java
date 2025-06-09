package ikw.school.busreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReservationChartController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ 1. 시간대별 예약 평균 차트 (lineId로 조회, 최근 20일 또는 전체)
    @GetMapping("/reservations/chart/line")
    public List<Map<String, Object>> getAverageReservationsByLine(
            @RequestParam Long lineId,
            @RequestParam String type
    ) {
        String findStartSql = "SELECT MIN(reserved_at) FROM (SELECT DISTINCT DATE(reserved_at) AS reserved_at FROM BusReservation WHERE reservation_status = '예약완료' ORDER BY reserved_at DESC LIMIT 20) AS recent";
        LocalDate startDate = jdbcTemplate.queryForObject(findStartSql, LocalDate.class);

        String sql = """
        SELECT 
            d.id AS departure_time_id,
            DATE_FORMAT(d.departure, '%H:%i') AS time,
            ROUND(SUM(sub.reserved_count) / COUNT(DISTINCT sub.date), 1) AS avg_reserved_count
        FROM DepartureTime d
        JOIN (
            SELECT 
                br.departure_time_id,
                DATE(br.reserved_at) AS date,
                COUNT(*) AS reserved_count
            FROM BusReservation br
            WHERE br.reservation_status = '예약완료'
              AND br.reservation_type = ?
              AND br.reserved_at >= ?
            GROUP BY br.departure_time_id, date
        ) AS sub ON d.id = sub.departure_time_id
        WHERE d.line_id = ?
        GROUP BY d.id, time
        HAVING avg_reserved_count > 0
        ORDER BY d.departure
        """;

        return jdbcTemplate.queryForList(sql, type, startDate, lineId);
    }

    // ✅ 2. 요일별  평균 예약자 수 (주말 제외, 최근 20일 또는 전체, 요일 필터 가능)
    @GetMapping("/reservations/chart/weekday")
    public List<Map<String, Object>> getAverageReservationsByWeekday(
            @RequestParam Long lineId,
            @RequestParam String type,
            @RequestParam(required = false) String weekday
    ) {
        String findStartSql = """
        SELECT MIN(reserved_at)
        FROM (
            SELECT DISTINCT DATE(reserved_at) AS reserved_at
            FROM BusReservation
            WHERE reservation_status = '예약완료'
            ORDER BY reserved_at DESC
            LIMIT 20
        ) AS recent
    """;
        LocalDate startDate = jdbcTemplate.queryForObject(findStartSql, LocalDate.class);

        // ✅ 한글 요일을 DAYOFWEEK 숫자로 매핑
        String weekdayFilter = "";
        if (weekday != null) {
            int weekdayNum = switch (weekday) {
                case "월" -> 2;
                case "화" -> 3;
                case "수" -> 4;
                case "목" -> 5;
                case "금" -> 6;
                default -> 0; // 잘못된 입력 무시
            };
            if (weekdayNum >= 2 && weekdayNum <= 6) {
                weekdayFilter = "AND DAYOFWEEK(br.reserved_at) = " + weekdayNum;
            }
        }

        String sql = """
        SELECT
          CASE DAYOFWEEK(sub.date)
            WHEN 2 THEN '월' WHEN 3 THEN '화' WHEN 4 THEN '수' WHEN 5 THEN '목' WHEN 6 THEN '금'
          END AS weekday,
          DATE_FORMAT(d.departure, '%%H:%%i') AS time,
          ROUND(AVG(cnt), 1) AS avg_reservations
        FROM (
          SELECT
            br.departure_time_id,
            DATE(br.reserved_at) AS date,
            COUNT(*) AS cnt
          FROM BusReservation br
          WHERE br.reservation_status = '예약완료'
            AND br.reservation_type = ?
            AND br.reserved_at >= ?
            %s
          GROUP BY br.departure_time_id, date
        ) AS sub
        JOIN DepartureTime d ON d.id = sub.departure_time_id
        WHERE d.line_id = ?
          AND DAYOFWEEK(sub.date) BETWEEN 2 AND 6
        GROUP BY weekday, time
        ORDER BY FIELD(weekday, '월','화','수','목','금'), time
    """.formatted(weekdayFilter);  // ✅ 필터 문자열 삽입

        return jdbcTemplate.queryForList(sql, type, startDate, lineId);
    }


}


