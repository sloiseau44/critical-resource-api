package com.criticalresource.criticalresourceapi.domain.audit;

import com.criticalresource.criticalresourceapi.AbstractIntegrationTest;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceCategory;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceRequest;
import com.criticalresource.criticalresourceapi.domain.resource.ResourceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditLogIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private AuditLogRepository auditLogRepository;

    protected HttpHeaders headers;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();
        headers = createAdminHeaders();
    }

    @Test
    public void should_return_audit_logs_after_creating_resource() {
        // 1. Créer une ressource (déclenche un audit log)
        ResourceRequest request = ResourceRequest.builder()
                .name("Véhicule VB-01")
                .description("Test")
                .category(ResourceCategory.VEHICLE)
                .build();

        HttpEntity<ResourceRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity("/resources", entity, ResourceResponse.class);

        // 2. Vérifier que les audit logs existent
        ResponseEntity<List<AuditLogResponse>> response = restTemplate.exchange(
                "/audit-logs",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<AuditLogResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
