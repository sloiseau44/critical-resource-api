package com.criticalresource.criticalresourceapi.domain.audit;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AuditLogResponse {
    private Long id;

    private Long userId;

    private AuditAction action;

    private String entityName;

    private Long entityId;

    private LocalDate timestamp;
}
