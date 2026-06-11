package com.criticalresource.criticalresourceapi.domain.resource;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceResponse {
    private Long id;

    private String name;

    private String description;

    private ResourceCategory category;

    private ResourceStatus status;
}
