package com.criticalresource.criticalresourceapi.domain.audit;

import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {
    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    public void should_return_all_auditLog () {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.ADMIN)
                .build();

        List<AuditLog> auditLogList = List.of(
                AuditLog.builder().user(user).build(),
                AuditLog.builder().user(user).build()
        );

        when(auditLogRepository.findAll()).thenReturn(auditLogList);

        List<AuditLogResponse> listAuditLog = auditLogService.getAuditLogs();

        assertThat(listAuditLog).hasSize(2);

    }
}
