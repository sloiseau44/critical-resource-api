package com.criticalresource.criticalresourceapi.domain.resource;

import com.criticalresource.criticalresourceapi.domain.audit.AuditAction;
import com.criticalresource.criticalresourceapi.domain.audit.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    private final AuditLogService auditLogService;

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

        auditLogService.log(AuditAction.CREATE, resourceRequest.getName(), saved.getId());

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

        Resource saved = resourceRepository.save(updated);

        auditLogService.log(AuditAction.UPDATE, resourceRequest.getName(), saved.getId());

        return toResponse(saved);
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

        Resource saved = resourceRepository.save(updated);

        auditLogService.log(AuditAction.UPDATE, saved.getName(), saved.getId());

        return toResponse(saved);
    }

    private Resource findResourceOrThrow(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }
}
