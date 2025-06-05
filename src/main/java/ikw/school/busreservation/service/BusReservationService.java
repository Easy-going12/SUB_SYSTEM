package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.*;
import ikw.school.busreservation.repository.BusReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusReservationService {

    private final BusReservationRepository repository;
    private final LineService lineService;
    private final DepartureTimeService departureTimeService;
    private final StopService stopService;
    private final SeatService seatService;

    public BusReservationService(BusReservationRepository repository,
                                 LineService lineService,
                                 DepartureTimeService departureTimeService,
                                 StopService stopService,
                                 SeatService seatService) {
        this.repository = repository;
        this.lineService = lineService;
        this.departureTimeService = departureTimeService;
        this.stopService = stopService;
        this.seatService = seatService;
    }

    // ✅ 정확한 busId + seatNumber 기준으로 좌석 조회 및 예약
    @Transactional
    public void reserveSeat(String userId, String reservationType, int seatNumber,
                            Long lineId, Long departureTimeId, Long stopId) {

        Line line = lineService.getLineById(lineId.intValue());
        DepartureTime departureTime = departureTimeService.getById(departureTimeId.intValue());
        Stop stop = stopService.getStopById(stopId.intValue());
        Integer busId = departureTime.getBus().getId();

        // 🟡 핵심: busId + seatNumber 로 정확한 Seat 찾기
        Seat seat = seatService.getSeatByBusAndNumber(busId, seatNumber);

        BusReservation reservation = BusReservation.builder()
                .userId(userId)
                .reservationType(reservationType)
                .line(line)
                .departureTime(departureTime)
                .stop(stop)
                .seat(seat)
                .reservationStatus("예약완료")
                .reservedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        repository.save(reservation);
    }

    // 예약된 좌석번호만 반환 (busId 기준 필터 추가)
    public List<Integer> getReservedSeatNumbersByBus(Integer departureTimeId, Integer busId) {
        return repository.findByDepartureTimeId(departureTimeId).stream()
                .filter(r -> r.getSeat() != null && r.getSeat().getBus().getId().equals(busId))
                .map(r -> r.getSeat().getSeatNumber())
                .collect(Collectors.toList());
    }

    // 출발시간 기준 예약된 좌석 수 반환
    public int countReservedSeats(Integer departureTimeId) {
        return repository.countReservedSeatsByDepartureTimeId(departureTimeId);
    }

    public void saveReservation(BusReservation reservation) {
        repository.save(reservation);
    }

    public List<BusReservation> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}
