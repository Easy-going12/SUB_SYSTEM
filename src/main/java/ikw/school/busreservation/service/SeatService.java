package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Seat;
import ikw.school.busreservation.repository.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // busId와 seatNumber를 함께 받아서 좌석 조회
    public Seat getSeatByBusAndNumber(Integer busId, Integer seatNumber) {
        return seatRepository.findByBusIdAndSeatNumber(busId, seatNumber);
    }
}
