package com.criticalresource.criticalresourceapi.domain.reservation;

import com.criticalresource.criticalresourceapi.AbstractIntegrationTest;
import com.criticalresource.criticalresourceapi.domain.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class ReservationIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    private HttpHeaders headers;

    private Long userId;
    private Resource resource;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        resourceRepository.deleteAll();
        headers = createAdminHeaders();

        resource = resourceRepository.save(Resource.builder()
                .name("Véhicule VB-01")
                .description("Véhicule blindé")
                .category(ResourceCategory.VEHICLE)
                .status(ResourceStatus.AVAILABLE)
                .build());

        userId = userRepository.findByUsername("admin").get().getId();
    }

    @Test
    public void should_create_reservation() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .userId(userId)
                .resourceId(resource.getId())
                .startDate(today)
                .endDate(tomorrow)
                .build();

        HttpEntity<ReservationRequest> entity = new HttpEntity<>(reservationRequest, headers);

        ResponseEntity<ReservationResponse> response = restTemplate
                .postForEntity("/reservations", entity, ReservationResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResourceId()).isEqualTo(resource.getId());
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
    }

    @Test
    public void should_cancel_reservation() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .userId(userId)
                .resourceId(resource.getId())
                .startDate(today)
                .endDate(tomorrow)
                .build();

        HttpEntity<ReservationRequest> entity = new HttpEntity<>(reservationRequest, headers);

        ResponseEntity<ReservationResponse> created = restTemplate.postForEntity("/reservations", entity, ReservationResponse.class);

        ResponseEntity<ReservationResponse> canceled = restTemplate.exchange(
                "/reservations/" + created.getBody().getId() + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                ReservationResponse.class
        );

        assertThat(canceled.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(canceled.getBody().getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }
}
