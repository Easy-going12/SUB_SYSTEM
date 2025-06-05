package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    private String userId;
    private String password;
    private LocalDateTime loginTime;
    private Boolean success;
}
