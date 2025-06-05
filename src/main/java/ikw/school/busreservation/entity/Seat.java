package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @Column(name = "seat_type")
    private String seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    private Bus bus;
}
