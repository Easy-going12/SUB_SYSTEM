package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    // ✅ 회원가입 성공 시 리다이렉트
    @Test
    void testRegisterSuccess() throws Exception {
        // Arrange
        Mockito.when(authService.register(any(Member.class)))
                .thenReturn(ResponseEntity.ok("가입 성공"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .param("userId", "201901002")
                        .param("password", "Rkqnxhekdn1@")
                        .param("name", "리지윤")
                        .param("phoneNumber", "01011111111")
                        .param("campus", "경운대학교")
                        .param("role", "USER")
                        .param("isReserved", "예약제"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ✅ 아이디 중복 확인 API 테스트
    @Test
    void testCheckDuplicateId() throws Exception {
        // Arrange
        Mockito.when(authService.isUserIdDuplicate(eq("existing_user")))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/auth/check-id")
                        .param("userId", "existing_user"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
