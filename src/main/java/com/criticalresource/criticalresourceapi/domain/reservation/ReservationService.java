package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.domain.resource.Resource;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRepository;
import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final ResourceRepository resourceRepository;

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .resourceId(reservation.getResource().getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build();
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resource resource = resourceRepository.findById(reservationRequest.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        Reservation reservation = Reservation.builder()
                .user(user)
                .resource(resource)
                .startDate(reservationRequest.getStartDate())
                .endDate(reservationRequest.getEndDate())
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    public List<ReservationResponse> getReservations(User user) {
        Role role = user.getRole();
        if (role.equals(Role.ADMIN) || role.equals(Role.GESTIONNAIRE)) {
            return reservationRepository.findAll().stream().map(this::toResponse).toList();
        } else {
            return reservationRepository.findByUser(user).stream().map(this::toResponse).toList();
        }
    }
}
