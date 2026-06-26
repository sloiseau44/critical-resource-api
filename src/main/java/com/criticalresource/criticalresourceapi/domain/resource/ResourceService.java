package com.criticalresource.criticalresourceapi.domain.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    private ResourceResponse toResponse(Resource resource) {
        return ResourceResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .description(resource.getDescription())
                .category(resource.getCategory())
                .status(resource.getStatus())
                .build();
    }

    public List<ResourceResponse> getResources(ResourceStatus status, ResourceCategory category) {
        return resourceRepository.findAll().stream()
                .filter(resource -> status == null || resource.getStatus() == status)
                .filter(resource -> category == null || resource.getCategory() == category)
                .map(this::toResponse)
                .toList();
    }

    public ResourceResponse createResource(ResourceRequest resourceRequest) {
        Resource resource = Resource.builder()
                .name(resourceRequest.getName())
                .description(resourceRequest.getDescription())
                .category(resourceRequest.getCategory())
                .status(ResourceStatus.AVAILABLE)
                .build();

        Resource saved = resourceRepository.save(resource);
        return toResponse(saved);
    }

    public ResourceResponse getResourceById(Long id) {
        return resourceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }

    public ResourceResponse updateResource(Long id, ResourceRequest resourceRequest) {
        Resource existing = findResourceOrThrow(id);

        Resource updated = Resource.builder()
                .id(existing.getId())
                .name(resourceRequest.getName())
                .description(resourceRequest.getDescription())
                .category(resourceRequest.getCategory())
                .status(existing.getStatus())
                .build();

        return toResponse(resourceRepository.save(updated));
    }

    public ResourceResponse disableResource(Long id) {
        Resource existing = findResourceOrThrow(id);

        Resource updated = Resource.builder()
                .id(existing.getId())
                .name(existing.getName())
                .description(existing.getDescription())
                .category(existing.getCategory())
                .status(ResourceStatus.DISABLED)
                .build();

        return toResponse(resourceRepository.save(updated));
    }

    private Resource findResourceOrThrow(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }
}
