package com.criticalresource.criticalresourceapi.domain.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {
    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        when(resourceRepository.findAll()).thenReturn(List.of(
                Resource.builder().name("Véhicule VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build(),
                Resource.builder().name("Équipement EQ-01").category(ResourceCategory.EQUIPMENT).status(ResourceStatus.AVAILABLE).build(),
                Resource.builder().name("Salle S-01").category(ResourceCategory.FACILITY).status(ResourceStatus.MAINTENANCE).build()
        ));
    }

    @Test
    public void should_return_all_resources_when_no_filter() {
        List<ResourceResponse> listResource = resourceService.getResources(null, null);

        assertThat(listResource).hasSize(3);
    }

    @Test
    public void should_return_resources_filtered_by_status() {
        List<ResourceResponse> listResource = resourceService.getResources(ResourceStatus.AVAILABLE, null);

        assertThat(listResource).hasSize(2);
    }

    @Test
    public void should_return_resources_filtered_by_category() {
        List<ResourceResponse> listResource = resourceService.getResources(null, ResourceCategory.FACILITY);

        assertThat(listResource).hasSize(1);
    }
}
