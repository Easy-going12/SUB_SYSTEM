package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void testMyPage_authenticatedUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user1");

        Member member = new Member();
        member.setUserId("user1");
        member.setName("홍길동");

        when(memberService.findByUserId("user1")).thenReturn(member);

        mockMvc.perform(get("/member/mypage").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage"))
                .andExpect(model().attributeExists("member"));
    }

    @Test
    void testUpdatePhoneForm_authenticatedUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user1");

        when(memberService.findByUserId("user1")).thenReturn(new Member());

        mockMvc.perform(get("/member/update-phone").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("update-phone"))
                .andExpect(model().attributeExists("member"));
    }

    @Test
    void testUpdatePhone() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user1");

        mockMvc.perform(post("/member/update-phone")
                        .param("phoneNumber", "01012345678")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/mypage"));
    }

    @Test
    void testChangePasswordForm() throws Exception {
        mockMvc.perform(get("/member/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password"));
    }

    @Test
    void testCheckDuplicateId() throws Exception {
        when(memberService.existsByUserId("user1")).thenReturn(true);

        mockMvc.perform(get("/member/check-id").param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
