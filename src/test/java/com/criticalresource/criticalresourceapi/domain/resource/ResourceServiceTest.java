package com.criticalresource.criticalresourceapi.domain.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {
    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    private List<Resource> buildResourceList() {
        return List.of(
                Resource.builder().name("Véhicule VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build(),
                Resource.builder().name("Équipement EQ-01").category(ResourceCategory.EQUIPMENT).status(ResourceStatus.AVAILABLE).build(),
                Resource.builder().name("Salle S-01").category(ResourceCategory.FACILITY).status(ResourceStatus.MAINTENANCE).build()
        );
    }

    @Test
    public void should_return_all_resources_when_no_filter() {
        when(resourceRepository.findAll()).thenReturn(buildResourceList());

        List<ResourceResponse> listResource = resourceService.getResources(null, null);

        assertThat(listResource).hasSize(3);
    }

    @Test
    public void should_return_resources_filtered_by_status() {
        when(resourceRepository.findAll()).thenReturn(buildResourceList());

        List<ResourceResponse> listResource = resourceService.getResources(ResourceStatus.AVAILABLE, null);

        assertThat(listResource).hasSize(2);
    }

    @Test
    public void should_return_resources_filtered_by_category() {
        when(resourceRepository.findAll()).thenReturn(buildResourceList());

        List<ResourceResponse> listResource = resourceService.getResources(null, ResourceCategory.FACILITY);

        assertThat(listResource).hasSize(1);
    }

    @Test
    public void should_create_resource() {
        ResourceRequest resourceRequest = ResourceRequest.builder()
                .name("Véhicule blindé VB-03")
                .description("Véhicule blindé de transport de troupes")
                .category(ResourceCategory.VEHICLE)
                .build();

        when(resourceRepository.save(any(Resource.class))).thenReturn(
                Resource.builder()
                        .id(1L)
                        .name("Véhicule blindé VB-03")
                        .description("Véhicule blindé de transport de troupes")
                        .category(ResourceCategory.VEHICLE)
                        .status(ResourceStatus.AVAILABLE)
                        .build()
        );

        ResourceResponse resourceResponse = resourceService.createResource(resourceRequest);

        assertThat(resourceResponse.getName()).isEqualTo("Véhicule blindé VB-03");
        assertThat(resourceResponse.getDescription()).isEqualTo("Véhicule blindé de transport de troupes");
        assertThat(resourceResponse.getCategory()).isEqualTo(ResourceCategory.VEHICLE);
        assertThat(resourceResponse.getStatus()).isEqualTo(ResourceStatus.AVAILABLE);
    }
}
