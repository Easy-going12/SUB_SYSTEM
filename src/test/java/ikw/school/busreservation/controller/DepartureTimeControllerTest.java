package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.DepartureTime;
import ikw.school.busreservation.service.DepartureTimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartureTimeController.class)
public class DepartureTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartureTimeService departureTimeService;

    @Test
    void testGetDeparturesByLineIdAndType() throws Exception {
        // Arrange: 가짜 응답 데이터 준비
        DepartureTime d1 = new DepartureTime();
        d1.setId(1);
        d1.setReservationType("등교");

        DepartureTime d2 = new DepartureTime();
        d2.setId(2);
        d2.setReservationType("등교");

        when(departureTimeService.getDepartureTimesByLineAndType(101, "등교"))
                .thenReturn(List.of(d1, d2));

        // Act & Assert: GET 요청 보내고 JSON 길이 확인
        mockMvc.perform(get("/reservation/ajax/departures")
                        .param("lineId", "101")
                        .param("type", "등교"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].reservationType").value("등교"));
    }
}
