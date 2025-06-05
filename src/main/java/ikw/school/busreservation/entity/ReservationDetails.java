package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private BusReservation busReservatioin;

    private String reservationType;

    @ManyToOne
    private Line line;

    @ManyToOne
    private DepartureTime departureTime;

    private Integer seatNumber;
    private Timestamp reservedAt;
}
