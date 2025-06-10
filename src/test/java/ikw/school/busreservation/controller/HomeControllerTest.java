package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.entity.MessageBox;
import ikw.school.busreservation.entity.NoticeBoard;
import ikw.school.busreservation.service.MemberService;
import ikw.school.busreservation.service.MessageBoxService;
import ikw.school.busreservation.service.NoticeBoardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeBoardService noticeBoardService;

    @MockBean
    private MessageBoxService messageBoxService;

    @MockBean
    private MemberService memberService;

    @Test
    void testHomePage() throws Exception {
        // Arrange
        String userId = "testuser";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        // 가짜 member
        Member member = new Member();
        member.setName("홍길동");

        // 가짜 공지사항
        NoticeBoard notice1 = new NoticeBoard();
        notice1.setTitle("공지사항1");

        // 가짜 쪽지
        MessageBox message1 = new MessageBox();
        message1.setContent("쪽지1");

        // Mock 설정
        when(memberService.findByUserId(userId)).thenReturn(member);
        when(noticeBoardService.findRecentNotices(3)).thenReturn(List.of(notice1));
        when(messageBoxService.getReceivedMessages(userId)).thenReturn(List.of(message1));

        // Act & Assert
        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attributeExists("recentNotices"))
                .andExpect(model().attributeExists("recentMessages"))
                .andExpect(model().attributeExists("qrCode"));
    }
}
