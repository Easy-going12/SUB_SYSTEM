package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JoinController.class)
class JoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testShowJoinPage() throws Exception {
        // Arrange → 생략 가능: 단순 GET

        // Act & Assert
        mockMvc.perform(get("/join"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(view().name("join")) // join.html 뷰 반환
                .andExpect(model().attributeExists("member")); // member 객체가 모델에 포함되어야 함
    }
}
