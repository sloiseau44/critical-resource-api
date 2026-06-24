package com.criticalresource.criticalresourceapi.domain.reservation;


import com.criticalresource.criticalresourceapi.domain.resource.Resource;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceCategory;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRepository;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceStatus;
import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void should_create_reservation() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .userId(1l)
                .resourceId(1L)
                .startDate(today)
                .endDate(tomorrow)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(
                User.builder().id(1L).username("jdupont").email("jdupont@test.fr").password("encoded").role(Role.OPERATEUR).build()
        ));

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(
                Resource.builder().id(1L).name("VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build()
        ));

        when(reservationRepository.save(any(Reservation.class))).thenReturn(
                Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .build()
        );

        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);

        assertThat(reservationResponse.getUserId()).isEqualTo(1L);
        assertThat(reservationResponse.getResourceId()).isEqualTo(1L);
        assertThat(reservationResponse.getStartDate()).isEqualTo(today);
        assertThat(reservationResponse.getEndDate()).isEqualTo(tomorrow);


    }
}