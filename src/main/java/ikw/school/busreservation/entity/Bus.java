package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String busNumber;
    private Integer capacity;
    private String busType;
}
