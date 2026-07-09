package com.criticalresource.criticalresourceapi.domain.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private ResourceCategory category;
}
