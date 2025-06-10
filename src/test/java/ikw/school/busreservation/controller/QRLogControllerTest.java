package ikw.school.busreservation.controller;

import ikw.school.busreservation.entity.QRLog;
import ikw.school.busreservation.service.QRLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QRLogController.class)
class QRLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QRLogService qrLogService;

    @Test
    @DisplayName("탑승내역 페이지 반환")
    void testShowBoardingHistory() throws Exception {
        QRLog log = new QRLog();
        log.setId(1L);
        log.setUserId("user123");

        when(qrLogService.getLogsForUser("user123"))
                .thenReturn(List.of(log));

        mockMvc.perform(get("/boarding")
                        .sessionAttr("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(view().name("boarding-history"))
                .andExpect(model().attributeExists("qrLogs"));
    }

    @Test
    @DisplayName("탑승내역 삭제 요청 처리")
    void testDeleteQrLog() throws Exception {
        mockMvc.perform(post("/boarding/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boarding"));

        verify(qrLogService).deleteLogById(1L);
    }
}
