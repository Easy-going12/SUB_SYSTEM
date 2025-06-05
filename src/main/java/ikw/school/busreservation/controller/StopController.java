package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Stop;
import ikw.school.busreservation.service.StopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation/ajax")
public class StopController {

    private final StopService stopService;

    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    // ✅ 출발시간 ID로 정류장 목록 조회
    @GetMapping("/stops")
    public List<Stop> getStopsByDeparture(@RequestParam("departureId") Integer departureId) {
        return stopService.getStopsByDeparture(departureId);
    }
}
