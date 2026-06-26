package com.criticalresource.criticalresourceapi.domain.audit;

import com.criticalresource.criticalresourceapi.domain.reservation.Reservation;
import com.criticalresource.criticalresourceapi.domain.reservation.ReservationRepository;
import com.criticalresource.criticalresourceapi.domain.reservation.ReservationStatus;
import com.criticalresource.criticalresourceapi.domain.resource.Resource;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceCategory;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRepository;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceStatus;
import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuditLogRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void should_save_and_find_log_by_user () {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        userRepository.save(user);

        AuditLog auditLogReservation = AuditLog.builder()
                .user(user)
                .action(AuditAction.CREATE)
                .entityName("Reservation")
                .entityId(32L)
                .timestamp(LocalDate.now())
                .build();

        auditLogRepository.save(auditLogReservation);

        AuditLog auditLogResource = AuditLog.builder()
                .user(user)
                .action(AuditAction.CREATE)
                .entityName("Resource")
                .entityId(2L)
                .timestamp(LocalDate.now())
                .build();

        auditLogRepository.save(auditLogResource);

        assertThat(auditLogRepository.findByUser(user)).hasSize(2);

    }
}
