package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.QRLog;
import ikw.school.busreservation.repository.QRLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QRLogService {

    private final QRLogRepository qrLogRepository;

    public QRLogService(QRLogRepository qrLogRepository) {
        this.qrLogRepository = qrLogRepository;
    }

    public List<QRLog> getLogsForUser(String userId) {
        return qrLogRepository.findByQrTextContainingOrderByScanTimeDesc(userId);
    }

    public void deleteLogById(Long id) {
        qrLogRepository.deleteById(id);
    }

    public boolean hasScannedQr(String userId, Long reservationId) {
        String keyword = "userId=" + userId + ";reservationId=" + reservationId;
        return qrLogRepository.existsByQrTextContaining(keyword);
    }

}

