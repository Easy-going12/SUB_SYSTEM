package ikw.school.busreservation.controller;

import ikw.school.busreservation.service.LoginService;
import ikw.school.busreservation.service.MemberService;
import ikw.school.busreservation.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private MemberService memberService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        when(loginService.login("admin", "pass123")).thenReturn(true);
        Member member = new Member();
        member.setName("관리자");
        when(memberService.findByUserId("admin")).thenReturn(member);

        // Act & Assert
        mockMvc.perform(post("/login")
                        .param("userId", "admin")
                        .param("password", "pass123")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testLoginFailure() throws Exception {
        when(loginService.login("wrong", "wrong")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("userId", "wrong")
                        .param("password", "wrong")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testLogout() throws Exception {
        session.setAttribute("userId", "admin");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
