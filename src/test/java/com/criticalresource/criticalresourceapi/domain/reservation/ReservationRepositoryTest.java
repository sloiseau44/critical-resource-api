package com.criticalresource.criticalresourceapi.domain.reservation;

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
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void should_save_and_find_reservation_by_user() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        Resource resource = Resource.builder()
                .name("Véhicule blindé VB-03")
                .description("Véhicule blindé de transport de troupes")
                .category(ResourceCategory.VEHICLE)
                .status(ResourceStatus.AVAILABLE)
                .build();

        userRepository.save(user);
        resourceRepository.save(resource);

        Reservation reservation = Reservation.builder()
                .user(user)
                .resource(resource)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(ReservationStatus.CONFIRMED)
                .build();

        reservationRepository.save(reservation);

        assertThat(reservationRepository.findByUser(user)).hasSize(1);
    }
}
