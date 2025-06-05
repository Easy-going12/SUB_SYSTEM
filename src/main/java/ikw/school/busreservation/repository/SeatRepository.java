package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Seat findByBusIdAndSeatNumber(Integer busId, Integer seatNumber);
}
