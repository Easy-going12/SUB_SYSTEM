package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {
    @Id
    private String userId;

    private String password;
    private String name;
    private String phoneNumber;
    private String role;
    private String campus;

    @Enumerated(EnumType.STRING)
    private ReservationType isReserved;

    @Column(name = "penalty_count", nullable = false)
    private int penaltyCount = 0;

    public enum ReservationType {
        예약제, 비예약제
    }
}
