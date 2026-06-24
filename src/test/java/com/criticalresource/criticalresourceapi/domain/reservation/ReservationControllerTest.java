package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.auth.JwtService;
import com.criticalresource.criticalresourceapi.domain.reservation.ReservationController;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRepository;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(value = ReservationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ResourceRepository resourceRepository;

    @Test
    public void should_return_201_when_creating_reservation() throws Exception {
        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(
                ReservationResponse.builder()
                        .id(1L)
                        .userId(1L)
                        .resourceId(1L)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .build()
        );

        String today = LocalDate.now().toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"resourceId\":1,\"startDate\":\"" + today + "\",\"endDate\":\"" + tomorrow + "\"}"))
                .andExpect(status().isCreated());
    }
}
