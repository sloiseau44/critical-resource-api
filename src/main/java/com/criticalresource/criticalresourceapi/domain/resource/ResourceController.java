package com.criticalresource.criticalresourceapi.domain.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
