package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.MessageBox;
import ikw.school.busreservation.repository.MessageBoxRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageBoxService {

    private final MessageBoxRepository messageBoxRepository;

    public MessageBoxService(MessageBoxRepository messageBoxRepository) {
        this.messageBoxRepository = messageBoxRepository;
    }

    // ✅ 받은 쪽지 목록
    public List<MessageBox> getReceivedMessages(String receiverId) {
        return messageBoxRepository.findByReceiverIdOrderBySentAtDesc(receiverId);
    }

    // ✅ 쪽지 저장 (작성)
    public void sendMessage(MessageBox message) {
        message.setSentAt(LocalDateTime.now());
        message.setRead(false); // 새 쪽지는 안읽음 상태로
        messageBoxRepository.save(message);
    }

    // ✅ 쪽지 단건 조회
    public MessageBox getMessageById(Integer id) {
        Optional<MessageBox> result = messageBoxRepository.findById(id);
        return result.orElse(null);
    }

    // ✅ 읽음 처리
    public void markAsRead(Integer id) {
        MessageBox message = getMessageById(id);
        if (message != null && !message.isRead()) {
            message.setRead(true);
            messageBoxRepository.save(message);
        }
    }

    // ✅ 쪽지 삭제 (물리적 삭제)
    public void deleteMessage(Integer id) {
        messageBoxRepository.deleteById(id);
    }
}
