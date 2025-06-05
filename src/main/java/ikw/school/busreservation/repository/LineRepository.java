package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Integer> {
    List<Line> findByNameContaining(String region); // 예: "구미", "대구"
}
