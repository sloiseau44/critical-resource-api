package com.criticalresource.criticalresourceapi.domain.resource;

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

    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(@RequestBody ResourceRequest resourceRequest) {
        ResourceResponse resourceResponse = resourceService.createResource(resourceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceResponse);
    }
}
