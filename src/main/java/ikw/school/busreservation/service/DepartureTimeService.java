package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.DepartureTime;
import ikw.school.busreservation.repository.DepartureTimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartureTimeService {
    private final DepartureTimeRepository repository;

    public DepartureTimeService(DepartureTimeRepository repository) {
        this.repository = repository;
    }

    // 기존 방식 (단일 ID 조회)
    public DepartureTime getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // AJAX 등에서 사용하는 동적 조회 방식
    public List<DepartureTime> getDepartureTimesByLineAndType(Integer lineId, String reservationType) {
        return repository.findByLineIdAndReservationTypeOrderByDepartureAsc(lineId, reservationType);
    }
}
