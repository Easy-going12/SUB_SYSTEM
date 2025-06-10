package ikw.school.busreservation.controller;

import ikw.school.busreservation.service.*;
import ikw.school.busreservation.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusReservationController.class)
public class BusReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;
    @MockBean
    private DepartureTimeService departureTimeService;
    @MockBean
    private StopService stopService;
    @MockBean
    private SeatService seatService;
    @MockBean
    private BusReservationService busReservationService;

    @Test
    void testShowReservationPage() throws Exception {
        when(lineService.getAllLines()).thenReturn(List.of());
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"));
    }

    @Test
    void testSubmitReservationSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "testuser");

        Line line = new Line(); line.setId(1);
        DepartureTime departureTime = new DepartureTime();
        Bus bus = new Bus(); bus.setId(99); departureTime.setBus(bus);
        Stop stop = new Stop(); stop.setId(1);
        Seat seat = new Seat(); seat.setId(3);

        when(lineService.getLineById(1)).thenReturn(line);
        when(departureTimeService.getById(100)).thenReturn(departureTime);
        when(stopService.getStopById(5)).thenReturn(stop);
        when(seatService.getSeatByBusAndNumber(99, 10)).thenReturn(seat);

        mockMvc.perform(post("/reservation/submit")
                        .param("reservationType", "등교")
                        .param("lineId", "1")
                        .param("departureTimeId", "100")
                        .param("stopId", "5")
                        .param("seatNumber", "10")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservation/history"));
    }

    @Test
    void testGetDepartureTimesByLineAndType() throws Exception {
        when(departureTimeService.getDepartureTimesByLineAndType(1, "등교"))
                .thenReturn(List.of());

        mockMvc.perform(get("/reservation/departures")
                        .param("lineId", "1")
                        .param("type", "등교"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStopsByLineAndDeparture() throws Exception {
        when(stopService.getStopsByLineAndDeparture(1, 100))
                .thenReturn(List.of());

        mockMvc.perform(get("/reservation/stations")
                        .param("lineId", "1")
                        .param("departureTimeId", "100"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservedSeats() throws Exception {
        when(busReservationService.getReservedSeatNumbersByBus(100, 99))
                .thenReturn(List.of(1, 2, 3));

        mockMvc.perform(get("/reservation/seats")
                        .param("departureTimeId", "100")
                        .param("busId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
