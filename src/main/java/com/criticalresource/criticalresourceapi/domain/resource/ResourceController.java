package com.criticalresource.criticalresourceapi.domain.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getResources(
            @RequestParam(required = false) ResourceStatus status,
            @RequestParam(required = false) ResourceCategory category
    ){
        List<ResourceResponse> resources = resourceService.getResources(status, category);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Long id) {
        ResourceResponse resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody ResourceRequest resourceRequest) {
        ResourceResponse resourceResponse = resourceService.createResource(resourceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> updateResource(@PathVariable Long id, @Valid @RequestBody ResourceRequest resourceRequest) {
        ResourceResponse resource = resourceService.updateResource(id, resourceRequest);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResourceResponse> disableResource(@PathVariable Long id) {
        ResourceResponse resource = resourceService.disableResource(id);
        return ResponseEntity.ok(resource);
    }
}
