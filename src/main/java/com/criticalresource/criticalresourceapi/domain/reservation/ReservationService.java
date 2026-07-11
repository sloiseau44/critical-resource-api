package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.domain.audit.AuditAction;
import com.criticalresource.criticalresourceapi.domain.audit.AuditLogService;
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

    private final AuditLogService auditLogService;

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .resourceId(reservation.getResource().getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .status(reservation.getStatus())
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
                .status(ReservationStatus.PENDING)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        auditLogService.log(AuditAction.CREATE, "Reservation", saved.getId());

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

    public ReservationResponse cancelReservation(Long id, User user) {
        Reservation existing = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));

        Role role = user.getRole();

        Reservation updated;

        if (existing.getUser().getId().equals(user.getId())
                || Role.ADMIN.equals(role)
                || Role.GESTIONNAIRE.equals(role)) {
            updated = Reservation.builder()
                    .id(existing.getId())
                    .user(existing.getUser())
                    .resource(existing.getResource())
                    .startDate(existing.getStartDate())
                    .endDate(existing.getEndDate())
                    .status(ReservationStatus.CANCELLED)
                    .build();
        } else {
            throw new RuntimeException("User not authorized to cancel the reservation " + id);
        }

        auditLogService.log(AuditAction.UPDATE, "Reservation", updated.getId());

        return toResponse(reservationRepository.save(updated));
    }
}
