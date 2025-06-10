package ikw.school.busreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.entity.NoticeBoard;
import ikw.school.busreservation.service.MemberService;
import ikw.school.busreservation.service.NoticeBoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoticeBoardController.class)
class NoticeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeBoardService noticeBoardService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공지사항 목록 페이지 반환")
    void testNoticeList() throws Exception {
        Mockito.when(noticeBoardService.findAll()).thenReturn(List.of(new NoticeBoard()));
        mockMvc.perform(get("/notice"))
                .andExpect(status().isOk())
                .andExpect(view().name("notice"))
                .andExpect(model().attributeExists("notices"));
    }

    @Test
    @DisplayName("공지사항 상세 보기 - 성공")
    void testNoticeDetail_Success() throws Exception {
        NoticeBoard notice = new NoticeBoard();
        notice.setId(1);
        Mockito.when(noticeBoardService.findById(1)).thenReturn(notice);

        mockMvc.perform(get("/notice/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("notice-detail"))
                .andExpect(model().attributeExists("notice"));
    }

    @Test
    @DisplayName("공지사항 상세 보기 - 실패")
    void testNoticeDetail_NotFound() throws Exception {
        Mockito.when(noticeBoardService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/notice/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notice"));
    }

    @Test
    @DisplayName("공지사항 작성 폼 반환")
    void testWriteForm() throws Exception {
        mockMvc.perform(get("/notice/write"))
                .andExpect(status().isOk())
                .andExpect(view().name("notice-write"))
                .andExpect(model().attributeExists("notice"));
    }

    @Test
    @DisplayName("공지사항 작성 처리")
    void testSubmitNotice() throws Exception {
        Member member = new Member();
        member.setName("홍길동");

        Mockito.when(memberService.findByUserId("user123")).thenReturn(member);

        mockMvc.perform(post("/notice/write")
                        .sessionAttr("userId", "user123")
                        .param("title", "공지 제목")
                        .param("content", "공지 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notice"));

        Mockito.verify(noticeBoardService).save(any(NoticeBoard.class));
    }

    @Test
    @DisplayName("공지사항 수정 폼")
    void testEditForm() throws Exception {
        NoticeBoard notice = new NoticeBoard();
        notice.setId(1);
        Mockito.when(noticeBoardService.findById(1)).thenReturn(notice);

        mockMvc.perform(get("/notice/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("notice-edit"))
                .andExpect(model().attributeExists("notice"));
    }

    @Test
    @DisplayName("공지사항 수정 처리")
    void testUpdateNotice() throws Exception {
        NoticeBoard notice = new NoticeBoard();
        notice.setId(1);
        notice.setTitle("Old Title");
        notice.setContent("Old Content");

        Member member = new Member();
        member.setName("관리자");

        Mockito.when(noticeBoardService.findById(1)).thenReturn(notice);
        Mockito.when(memberService.findByUserId("admin")).thenReturn(member);

        mockMvc.perform(post("/notice/edit/1")
                        .sessionAttr("userId", "admin")
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notice/1"));

        Mockito.verify(noticeBoardService).save(any(NoticeBoard.class));
    }

    @Test
    @DisplayName("공지사항 삭제 처리")
    void testDeleteNotice() throws Exception {
        mockMvc.perform(post("/notice/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notice"));

        Mockito.verify(noticeBoardService).delete(1);
    }
}
