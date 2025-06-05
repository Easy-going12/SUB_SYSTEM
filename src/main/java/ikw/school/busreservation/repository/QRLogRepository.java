package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.QRLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QRLogRepository extends JpaRepository<QRLog, Long> {
    List<QRLog> findByQrTextContainingOrderByScanTimeDesc(String userId);
    boolean existsByQrTextContaining(String userId);
}

