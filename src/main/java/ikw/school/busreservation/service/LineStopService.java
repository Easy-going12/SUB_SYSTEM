package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Line;
import ikw.school.busreservation.entity.LineStop;
import ikw.school.busreservation.repository.LineStopRepository;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

import java.util.List;

@Service
public class LineStopService {

    private final LineStopRepository lineStopRepository;

    public LineStopService(LineStopRepository lineStopRepository) {
        this.lineStopRepository = lineStopRepository;
    }

    public List<LineStop> getStopsByLine(Line line) {
        return lineStopRepository.findByLineOrderByStopOrderAsc(line);
    }

    public List<LocalTime> getArriveTimesByStopNameAndLineId(String stopName, Integer lineId) {
        return lineStopRepository.findArriveTimesByStopNameAndLineId(stopName, lineId);
    }
}
