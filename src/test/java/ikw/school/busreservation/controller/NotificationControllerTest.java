package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.Notification;
import ikw.school.busreservation.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService service;

    @Test
    @DisplayName("사용자 알림 전체 조회")
    void testGetNotifications() throws Exception {
        Notification n = new Notification();
        n.setNotificationId(1L);
        n.setUserId("user1");
        n.setContent("테스트 알림");  // ✅ 실제 필드
        n.setCategory("INFO");

        Mockito.when(service.getUserNotifications("user1"))
                .thenReturn(List.of(n));

        mockMvc.perform(get("/api/notifications/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].content").value("테스트 알림"));  // ✅ 변경된 경로
    }


    @Test
    @DisplayName("카테고리별 알림 조회")
    void testGetNotificationsByCategory() throws Exception {
        Notification n = new Notification();
        n.setUserId("user1");
        n.setCategory("EVENT");

        Mockito.when(service.getUserNotificationsByCategory("user1", "EVENT"))
                .thenReturn(List.of(n));

        mockMvc.perform(get("/api/notifications/user1/category/EVENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("EVENT"));
    }

    @Test
    @DisplayName("알림 생성")
    void testSendNotification() throws Exception {
        mockMvc.perform(post("/api/notifications")
                        .param("userId", "user1")
                        .param("message", "알림 내용")
                        .param("category", "INFO"))
                .andExpect(status().isOk());

        Mockito.verify(service).sendNotification("user1", "알림 내용", "INFO");
    }

    @Test
    @DisplayName("쪽지용 알림 전송")
    void testSendMessageNotification() throws Exception {
        mockMvc.perform(post("/api/notifications/message")
                        .param("senderId", "admin")
                        .param("receiverId", "user2")
                        .param("title", "쪽지 도착"))
                .andExpect(status().isOk());

        Mockito.verify(service).sendMessageNotification("admin", "user2", "쪽지 도착");
    }

    @Test
    @DisplayName("알림 확인 처리")
    void testCheckNotification() throws Exception {
        mockMvc.perform(patch("/api/notifications/check/100"))
                .andExpect(status().isOk());

        Mockito.verify(service).markAsChecked(100L);
    }

    @Test
    @DisplayName("알림 삭제")
    void testDeleteNotification() throws Exception {
        mockMvc.perform(delete("/api/notifications/200"))
                .andExpect(status().isOk());

        Mockito.verify(service).deleteNotification(200L);
    }
}
