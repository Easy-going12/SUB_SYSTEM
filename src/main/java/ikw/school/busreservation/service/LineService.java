package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Line;
import ikw.school.busreservation.repository.LineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll();
    }

    public List<Line> getLinesByRegion(String regionKeyword) {
        return lineRepository.findByNameContaining(regionKeyword);
    }

    public Line getLineById(Integer id) {
        return lineRepository.findById(id).orElse(null);
    }
}
