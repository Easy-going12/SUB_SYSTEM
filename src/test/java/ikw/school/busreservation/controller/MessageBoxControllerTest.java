package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.MessageBox;
import ikw.school.busreservation.service.MessageBoxService;
import ikw.school.busreservation.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageBoxController.class)
public class MessageBoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageBoxService messageBoxService;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testInbox_authenticatedUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user1");

        when(messageBoxService.getReceivedMessages("user1"))
                .thenReturn(List.of(new MessageBox()));

        mockMvc.perform(get("/messagebox").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("messagebox"))
                .andExpect(model().attributeExists("messages"));
    }

    @Test
    void testWriteForm() throws Exception {
        mockMvc.perform(get("/messagebox/send"))
                .andExpect(status().isOk())
                .andExpect(view().name("message-write"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void testSendMessage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "sender123");

        mockMvc.perform(post("/messagebox/send")
                        .param("receiverId", "receiver123")
                        .param("title", "Hello")
                        .param("content", "Test content")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messagebox"));

        verify(messageBoxService).sendMessage(any(MessageBox.class));
        verify(notificationService).sendMessageNotification("sender123", "receiver123", "Hello");
    }

    @Test
    void testViewMessage_found() throws Exception {
        MessageBox message = new MessageBox();
        message.setId(1);
        message.setTitle("Test");

        when(messageBoxService.getMessageById(1)).thenReturn(message);

        mockMvc.perform(get("/messagebox/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("message-detail"))
                .andExpect(model().attributeExists("message"));

        verify(messageBoxService).markAsRead(1);
    }

    @Test
    void testViewMessage_notFound() throws Exception {
        when(messageBoxService.getMessageById(99)).thenReturn(null);

        mockMvc.perform(get("/messagebox/view/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messagebox"));
    }

    @Test
    void testDeleteMessage() throws Exception {
        mockMvc.perform(post("/messagebox/delete/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messagebox"));

        verify(messageBoxService).deleteMessage(5);
    }
}
