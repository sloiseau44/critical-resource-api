package com.criticalresource.criticalresourceapi.domain.audit;

import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    private AuditLogResponse toResponse(AuditLog auditLog) {
        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .userId(auditLog.getUser().getId())
                .action(auditLog.getAction())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .timestamp(auditLog.getTimestamp())
                .build();
    }

    public List<AuditLogResponse> getAuditLogs() {
        return auditLogRepository.findAll().stream().map(this::toResponse).toList();
    }

    public void log(AuditAction action, String entityName, Long entityId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        auditLogRepository.save(AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .timestamp(LocalDate.now())
                .build());
    }
}
