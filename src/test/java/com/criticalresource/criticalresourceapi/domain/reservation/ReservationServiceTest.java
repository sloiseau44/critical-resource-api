package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.domain.audit.AuditLogService;
import com.criticalresource.criticalresourceapi.domain.resource.*;
import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Mock
    private AuditLogService auditLogService;

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

    @Test
    public void should_return_all_reservations_when_user_is_admin_or_gestionnaire() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.ADMIN)
                .build();


        Resource resource = Resource.builder()
                .id(1L)
                .name("Véhicule VB-01")
                .category(ResourceCategory.VEHICLE)
                .status(ResourceStatus.AVAILABLE)
                .build();

        List<Reservation> reservationList = List.of(
                Reservation.builder().user(user).resource(resource).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build(),
                Reservation.builder().user(user).resource(resource).startDate(LocalDate.now().plusDays(2)).endDate(LocalDate.now().plusDays(3)).build()
        );

        when(reservationRepository.findAll()).thenReturn(reservationList);

        List<ReservationResponse> listResource = reservationService.getReservations(user);

        assertThat(listResource).hasSize(2);
    }

    @Test
    public void should_return_only_user_reservations_when_user_is_operateur() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        Resource resource = Resource.builder()
                .id(1L)
                .name("Véhicule VB-01")
                .category(ResourceCategory.VEHICLE)
                .status(ResourceStatus.AVAILABLE)
                .build();

        List<Reservation> userReservations = List.of(
                Reservation.builder().user(user).resource(resource)
                        .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build()
        );

        when(reservationRepository.findByUser(user)).thenReturn(userReservations);

        List<ReservationResponse> listResource = reservationService.getReservations(user);

        assertThat(listResource).hasSize(1);
    }

    @Test
    public void should_cancel_reservation_when_admin_cancels_any_reservation() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(reservationRepository.findById(1L)).thenReturn(
                Optional.ofNullable(Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .build())
        );

        User admin = User.builder().role(Role.ADMIN).build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(
                Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .status(ReservationStatus.CANCELLED)
                        .build()
        );

        ReservationResponse reservationResponse = reservationService.cancelReservation(1L, admin);

        assertThat(reservationResponse.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    public void should_cancel_own_reservation() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        User user = User.builder().id(1L).build();

        when(reservationRepository.findById(1L)).thenReturn(
                Optional.ofNullable(Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .build())
        );

        when(reservationRepository.save(any(Reservation.class))).thenReturn(
                Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .status(ReservationStatus.CANCELLED)
                        .build()
        );

        ReservationResponse reservationResponse = reservationService.cancelReservation(1L, user);

        assertThat(reservationResponse.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(reservationResponse.getUserId()).isEqualTo(1L);
    }

    @Test
    public void should_throw_exception_when_operateur_cancels_other_user_reservation() {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        User user = User.builder().id(2L).role(Role.OPERATEUR).build();

        when(reservationRepository.findById(1L)).thenReturn(
                Optional.ofNullable(Reservation.builder()
                        .id(1L)
                        .user(User.builder().id(1L).build())
                        .resource(Resource.builder().id(1L).build())
                        .startDate(today)
                        .endDate(tomorrow)
                        .build())
        );

        assertThatThrownBy(() -> reservationService.cancelReservation(1L, user))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_throw_exception_when_reservation_not_found() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancelReservation(2L, User.builder().build()))
                .isInstanceOf(RuntimeException.class);
    }
}