package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Line;
import ikw.school.busreservation.entity.LineStop;
import ikw.school.busreservation.service.LineService;
import ikw.school.busreservation.service.LineStopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/station")
public class LineController {

    private final LineService lineService;
    private final LineStopService lineStopService;

    public LineController(LineService lineService, LineStopService lineStopService) {
        this.lineService = lineService;
        this.lineStopService = lineStopService;
    }

    // ✅ 전체 노선 조회 or 지역별 필터링 (정적 화면용)
    @GetMapping
    public String viewLines(@RequestParam(required = false) String region, Model model) {
        List<Line> lines;

        if (region != null && !region.isBlank()) {
            lines = lineService.getLinesByRegion(region);
            model.addAttribute("selectedRegion", region);
        } else {
            lines = lineService.getAllLines();
        }

        model.addAttribute("lines", lines);
        return "station"; // → templates/station.html
    }

    // ✅ 개별 노선 상세 페이지 (정류장 목록 + 시간 맵핑 포함)
    @GetMapping("/{lineId}")
    public String viewLineDetail(@PathVariable Integer lineId, Model model) {
        Line line = lineService.getLineById(lineId);
        if (line == null) return "redirect:/station";

        List<LineStop> stops = lineStopService.getStopsByLine(line);
        List<Line> allLines = lineService.getAllLines(); // 🔹 드롭다운용 노선 전체 목록

        // 🔥 각 정류장 이름에 대한 도착시간 리스트 맵 구성
        Map<String, List<LocalTime>> stopTimeMap = new LinkedHashMap<>();
        for (LineStop ls : stops) {
            String stopName = ls.getStop().getName();
            List<LocalTime> times = lineStopService.getArriveTimesByStopNameAndLineId(stopName, lineId);
            stopTimeMap.put(stopName, times);
        }

        model.addAttribute("line", line);
        model.addAttribute("stops", stops);
        model.addAttribute("allLines", allLines);
        model.addAttribute("stopTimeMap", stopTimeMap);

        return "line-detail"; // → templates/line-detail.html
    }

    // ✅ AJAX API: 노선 전체 목록 JSON으로 반환
    @ResponseBody
    @GetMapping("/ajax/list")
    public List<Line> getAllLinesAsJson() {
        return lineService.getAllLines();
    }
}
