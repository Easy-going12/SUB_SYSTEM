package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Stop;
import ikw.school.busreservation.service.StopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StopController.class)
class StopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StopService stopService;

    @Test
    @DisplayName("출발시간 ID로 정류장 목록 조회")
    void testGetStopsByDeparture() throws Exception {
        Stop stop1 = new Stop();
        stop1.setId(1);
        stop1.setName("정류장 A");

        Stop stop2 = new Stop();
        stop2.setId(2);
        stop2.setName("정류장 B");

        Mockito.when(stopService.getStopsByDeparture(eq(5)))
                .thenReturn(List.of(stop1, stop2));

        mockMvc.perform(get("/reservation/ajax/stops")
                        .param("departureId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("정류장 A"))
                .andExpect(jsonPath("$[1].name").value("정류장 B"));
    }
}
