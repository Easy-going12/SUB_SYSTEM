package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Stop;
import ikw.school.busreservation.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    private final StopRepository repository;

    public StopService(StopRepository repository) {
        this.repository = repository;
    }

    // 정류장 ID로 단건 조회
    public Stop getStopById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // ✅ 출발시간 ID 기준 정류장 목록 조회
    public List<Stop> getStopsByDeparture(Integer departureId) {
        return repository.findByDepartureTimeIdOrderByArriveTimeAsc(departureId);
    }

    public List<Stop> getStopsByLineAndDeparture(Integer lineId, Integer departureTimeId) {
        return repository.findStopsByLineAndDeparture(lineId, departureTimeId);
    }

}
