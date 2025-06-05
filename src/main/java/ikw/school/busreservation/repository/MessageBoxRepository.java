package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.MessageBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer> {

    // 받은 쪽지 리스트
    List<MessageBox> findByReceiverIdOrderBySentAtDesc(String receiverId);

    // 보낸 쪽지 리스트
    List<MessageBox> findBySenderIdOrderBySentAtDesc(String senderId);
}
