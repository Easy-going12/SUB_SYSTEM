package ikw.school.busreservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexPageLoads() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())                   // HTTP 200 확인
                .andExpect(view().name("Home"));              // 뷰 이름이 "Home"인지 확인
    }
}
