package com.criticalresource.criticalresourceapi.domain.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReservationRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long resourceId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
