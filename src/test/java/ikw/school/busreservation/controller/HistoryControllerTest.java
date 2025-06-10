package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.BusReservation;
import ikw.school.busreservation.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoryController.class)
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @Test
    void testShowReservationLog_authenticatedUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "admin");

        when(historyService.getReservationsByUserId("admin"))
                .thenReturn(List.of(new BusReservation()));  // 더미 데이터

        mockMvc.perform(get("/bus/log").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation-history"));
    }

    @Test
    void testShowReservationLog_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/bus/log"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testCancelReservation_authenticatedUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "testuser");

        doNothing().when(historyService).deleteReservation(123);

        mockMvc.perform(post("/bus/cancel")
                        .param("id", "123")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bus/log"));
    }

    @Test
    void testCancelReservation_unauthenticatedUser() throws Exception {
        mockMvc.perform(post("/bus/cancel").param("id", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
