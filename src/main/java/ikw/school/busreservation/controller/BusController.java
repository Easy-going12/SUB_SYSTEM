package ikw.school.busreservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.Map;
import java.util.HashMap;

@Controller
public class BusController {

    // 메모리에 위치 저장 (단일 사용자 기준)
    private final Map<String, Double> latestLocation = new HashMap<>();

    // 휴대폰이 버스 위치를 확인하는 페이지
    @GetMapping("/bus/location")
    public String showViewLocationPage(Model model) {
        model.addAttribute("lat", latestLocation.get("lat"));
        model.addAttribute("lng", latestLocation.get("lng"));
        return "view-location";
    }

    // ZED-F9R (Python)에서 위치 저장하는 API
    @PostMapping("/api/location")
    @ResponseBody
    public String saveLocation(@RequestBody Map<String, Double> body) {
        latestLocation.put("lat", body.get("lat"));
        latestLocation.put("lng", body.get("lng"));
        return "ok";
    }

    // 위치 조회 API (AJAX로 5초마다 호출됨)
    @GetMapping("/api/location/latest")
    @ResponseBody
    public Map<String, Double> getLatestLocation() {
        return latestLocation;
    }
}
