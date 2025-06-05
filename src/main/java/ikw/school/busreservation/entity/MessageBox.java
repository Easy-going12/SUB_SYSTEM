package ikw.school.busreservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MessageBox {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    private String senderId;    // 보낸 사람 (로그인한 사용자)
    private String receiverId;  // 받는 사람 (입력한 ID)

    private String title;       // 쪽지 제목
    private String content;     // 쪽지 본문

    private boolean isRead;     // 읽음 여부

    private LocalDateTime sentAt;  // 보낸 시간
}
