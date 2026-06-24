package com.criticalresource.criticalresourceapi.domain.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReservationResponse {
    private Long id;

    private Long userId;

    private Long resourceId;

    private LocalDate startDate;

    private LocalDate endDate;

    private ReservationStatus status;
}
