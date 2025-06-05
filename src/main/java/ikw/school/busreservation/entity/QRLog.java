package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Entity
@Table(name = "qr_logs")
public class QRLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String qrText;

    @Setter
    private LocalDateTime scanTime;

    // ✅ QR 텍스트를 해석하여 사용자에게 보여줄 문자열 생성
    public String getParsedDisplayText() {
        Map<String, String> params = parseQRText(qrText);

        String departure = params.getOrDefault("departure", "");
        String route = "";

        // 예시: line_id 값에 따라 노선 구분 (하교 / 등교)
        if (params.get("line_id") != null && params.get("line_id").startsWith("L")) {
            if (params.get("line_id").endsWith("6")) {
                route = "대구(교육관)(하교)";
            } else {
                route = "대구(교육관)(등교)";
            }
        }

        // 시간 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = scanTime.format(formatter);

        return route + " - " + formattedTime + " 탑승";
    }

    // ✅ 쿼리스트링 형식 파싱
    private Map<String, String> parseQRText(String qr) {
        Map<String, String> map = new HashMap<>();
        if (qr == null || !qr.contains("=")) return map;

        String[] pairs = qr.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}

