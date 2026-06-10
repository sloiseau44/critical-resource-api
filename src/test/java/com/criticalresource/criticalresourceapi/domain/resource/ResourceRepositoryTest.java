package com.criticalresource.criticalresourceapi.domain.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    void should_save_and_find_resource_by_status() {
        Resource resource = Resource.builder()
                .name("Véhicule blindé VB-03")
                .description("Véhicule blindé de transport de troupes")
                .category(ResourceCategory.VEHICLE)
                .status(ResourceStatus.AVAILABLE)
                .build();

        resourceRepository.save(resource);

        assertThat(resourceRepository.findByStatus(ResourceStatus.AVAILABLE)).hasSize(1);
    }
}