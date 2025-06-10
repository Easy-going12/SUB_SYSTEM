package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Line;
import ikw.school.busreservation.entity.LineStop;
import ikw.school.busreservation.entity.Stop;
import ikw.school.busreservation.service.LineService;
import ikw.school.busreservation.service.LineStopService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @MockBean
    private LineStopService lineStopService;

    @Test
    void testViewLines_NoRegion() throws Exception {
        when(lineService.getAllLines()).thenReturn(List.of(new Line()));

        mockMvc.perform(get("/station"))
                .andExpect(status().isOk())
                .andExpect(view().name("station"))
                .andExpect(model().attributeExists("lines"));
    }

    @Test
    void testViewLines_WithRegion() throws Exception {
        when(lineService.getLinesByRegion("서울")).thenReturn(List.of(new Line()));

        mockMvc.perform(get("/station?region=서울"))
                .andExpect(status().isOk())
                .andExpect(view().name("station"))
                .andExpect(model().attributeExists("lines"))
                .andExpect(model().attribute("selectedRegion", "서울"));
    }

    @Test
    void testViewLineDetail_ValidLineId() throws Exception {
        Line line = new Line();
        line.setId(1);
        line.setName("테스트노선");

        Stop stop = new Stop();
        stop.setName("정류장1");

        LineStop lineStop = new LineStop();
        lineStop.setStop(stop);  // 💡 NPE 방지!

        when(lineService.getLineById(1)).thenReturn(line);
        when(lineStopService.getStopsByLine(line)).thenReturn(List.of(lineStop));
        when(lineService.getAllLines()).thenReturn(List.of(line));
        when(lineStopService.getArriveTimesByStopNameAndLineId("정류장1", 1))
                .thenReturn(List.of(LocalTime.of(8, 0)));

        mockMvc.perform(get("/station/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("line-detail"))
                .andExpect(model().attributeExists("line"))
                .andExpect(model().attributeExists("stops"))
                .andExpect(model().attributeExists("allLines"))
                .andExpect(model().attributeExists("stopTimeMap"));
    }

    @Test
    void testViewLineDetail_InvalidLineId() throws Exception {
        when(lineService.getLineById(999)).thenReturn(null);

        mockMvc.perform(get("/station/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/station"));
    }

    @Test
    void testGetAllLinesAsJson() throws Exception {
        Line line = new Line();
        line.setId(1);
        line.setName("라인1");

        when(lineService.getAllLines()).thenReturn(List.of(line));

        ResultActions resultActions = mockMvc.perform(get("/station/ajax/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
