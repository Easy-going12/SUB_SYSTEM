package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "departuretime") // ✅ 실제 DB 테이블명 명시
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartureTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp departure;

    private Integer busNumber;

    @ManyToOne
    @JoinColumn(name = "bus_id") // 🚍 어떤 버스가 이 시간에 운행하는가
    private Bus bus;

    private Integer availableSeats;

    // ✅ 어떤 노선에 속하는 출발시간인가
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    // ✅ 등교 / 하교 구분용 예약 타입 (예: "등교", "하교")
    @Column(name = "reservation_type")
    private String reservationType;
}
