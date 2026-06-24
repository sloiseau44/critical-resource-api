package com.criticalresource.criticalresourceapi.domain.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReservationRequest {
    private Long userId;

    private Long resourceId;

    private LocalDate startDate;

    private LocalDate endDate;
}
