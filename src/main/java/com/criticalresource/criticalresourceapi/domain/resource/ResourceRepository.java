package com.criticalresource.criticalresourceapi.domain.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository  extends JpaRepository<Resource, Long> {

    List<Resource> findByStatus(ResourceStatus status);
}