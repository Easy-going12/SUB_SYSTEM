package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "busreservation")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BusReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;
    private String reservationType;

    @ManyToOne
    private Line line;

    @ManyToOne
    private DepartureTime departureTime;

    @ManyToOne
    private Stop stop;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private String reservationStatus;
    private Timestamp reservedAt;
}
