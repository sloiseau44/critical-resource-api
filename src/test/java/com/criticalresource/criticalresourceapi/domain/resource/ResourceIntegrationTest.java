package com.criticalresource.criticalresourceapi.domain.resource;

import com.criticalresource.criticalresourceapi.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class ResourceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private ResourceRepository resourceRepository;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        resourceRepository.deleteAll();

        headers = createAdminHeaders();
    }

    @Test
    public void should_create_resource() {
        ResourceRequest request = ResourceRequest.builder()
                .name("Véhicule VB-01")
                .description("Véhicule blindé")
                .category(ResourceCategory.VEHICLE)
                .build();

        HttpEntity<ResourceRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ResourceResponse> response = restTemplate
                .postForEntity("/resources", entity, ResourceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Véhicule VB-01");
        assertThat(response.getBody().getStatus()).isEqualTo(ResourceStatus.AVAILABLE);
    }

    @Test
    public void should_get_all_resources() {
        ResourceRequest request = ResourceRequest.builder()
                .name("Véhicule VB-01")
                .description("Véhicule blindé")
                .category(ResourceCategory.VEHICLE)
                .build();

        HttpEntity<ResourceRequest> entity = new HttpEntity<>(request, headers);

         restTemplate.postForEntity("/resources", entity, ResourceResponse.class);

        ResourceRequest request2 = ResourceRequest.builder()
                .name("Équipement EQ-01")
                .description("Equipement")
                .category(ResourceCategory.EQUIPMENT)
                .build();

        HttpEntity<ResourceRequest> entity2 = new HttpEntity<>(request2, headers);

        restTemplate.postForEntity("/resources", entity2, ResourceResponse.class);

        ResponseEntity<List<ResourceResponse>> response = restTemplate.exchange(
                "/resources",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ResourceResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void should_disable_resource() {
        ResourceRequest request = ResourceRequest.builder()
                .name("Véhicule VB-01")
                .description("Véhicule blindé")
                .category(ResourceCategory.VEHICLE)
                .build();

        HttpEntity<ResourceRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ResourceResponse> created = restTemplate.postForEntity("/resources", entity, ResourceResponse.class);
        Long id = created.getBody().getId();

        ResponseEntity<ResourceResponse> response = restTemplate.exchange(
                "/resources/"+id,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                ResourceResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo(ResourceStatus.DISABLED);
    }
}