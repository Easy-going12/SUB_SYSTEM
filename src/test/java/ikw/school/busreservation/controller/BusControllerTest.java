package ikw.school.busreservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusController.class)
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSaveLocationAndGetLatestLocation() throws Exception {
        // Arrange: JSON 데이터 준비
        String jsonBody = """
            {
              "lat": 35.9123,
              "lng": 128.8077
            }
        """;

        // Act 1: POST로 위치 저장
        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        // Act 2: GET으로 최신 위치 조회
        mockMvc.perform(get("/api/location/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lat", is(35.9123)))
                .andExpect(jsonPath("$.lng", is(128.8077)));
    }

    @Test
    void testShowViewLocationPage() throws Exception {
        // HTML 페이지 자체는 렌더링 확인만
        mockMvc.perform(get("/bus/location"))
                .andExpect(status().isOk())
                .andExpect(view().name("view-location"));
    }
}
