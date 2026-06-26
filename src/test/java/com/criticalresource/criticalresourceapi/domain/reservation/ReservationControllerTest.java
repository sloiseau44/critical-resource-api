package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.auth.JwtService;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRepository;
import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(value = ReservationController.class)
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
                        .with(user("admin"))
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"resourceId\":1,\"startDate\":\"" + today + "\",\"endDate\":\"" + tomorrow + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_return_200_when_getting_reservations() throws Exception {
        when(userRepository.findByUsername("jdupont")).thenReturn(
                Optional.ofNullable(User.builder().username("jdupont").build())
        );

        when(reservationService.getReservations(any(User.class))).thenReturn(List.of(
                ReservationResponse.builder().userId(1L).build()
        ));

        mockMvc.perform(get("/reservations")
                        .with(user("admin"))
                        .with(csrf())
                .param("username", "jdupont"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void should_return_200_when_canceling_reservation() throws Exception {
        when(userRepository.findByUsername("admin")).thenReturn(
                Optional.of(User.builder().id(1L).username("admin").role(Role.ADMIN).build())
        );

        when(reservationService.cancelReservation(eq(1L), any(User.class))).thenReturn(
                ReservationResponse.builder()
                        .id(1L)
                        .status(ReservationStatus.CANCELLED)
                        .build()
        );

        mockMvc.perform(put("/reservations/1/cancel")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
