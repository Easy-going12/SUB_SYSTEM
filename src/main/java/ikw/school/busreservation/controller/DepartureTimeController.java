package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.DepartureTime;
import ikw.school.busreservation.service.DepartureTimeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation/ajax")
public class DepartureTimeController {

    private final DepartureTimeService departureTimeService;

    public DepartureTimeController(DepartureTimeService departureTimeService) {
        this.departureTimeService = departureTimeService;
    }

    // 🚌 노선 ID와 타입(등교/하교)으로 출발시간 목록 조회
    @GetMapping("/departures")
    public List<DepartureTime> getDepartures(@RequestParam("lineId") Integer lineId,
                                             @RequestParam("type") String type) {
        return departureTimeService.getDepartureTimesByLineAndType(lineId, type);
    }
}
