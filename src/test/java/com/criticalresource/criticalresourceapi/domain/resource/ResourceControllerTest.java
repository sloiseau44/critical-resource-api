package com.criticalresource.criticalresourceapi.domain.resource;

import com.criticalresource.criticalresourceapi.auth.JwtService;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(value = ResourceController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ResourceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void should_return_200_when_getting_all_resources() throws Exception {
        when(resourceService.getResources(null, null)).thenReturn(List.of(
                ResourceResponse.builder().name("VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build(),
                ResourceResponse.builder().name("EQ-01").category(ResourceCategory.EQUIPMENT).status(ResourceStatus.AVAILABLE).build(),
                ResourceResponse.builder().name("S-01").category(ResourceCategory.FACILITY).status(ResourceStatus.MAINTENANCE).build()
        ));

        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void should_return_200_when_filtering_by_status() throws Exception {
        when(resourceService.getResources(ResourceStatus.AVAILABLE, null)).thenReturn(List.of(
                ResourceResponse.builder().name("VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build(),
                ResourceResponse.builder().name("EQ-01").category(ResourceCategory.EQUIPMENT).status(ResourceStatus.AVAILABLE).build()
        ));

        mockMvc.perform(get("/resources").param("status", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_return_200_when_filtering_by_category() throws Exception {
        when(resourceService.getResources(null, ResourceCategory.VEHICLE)).thenReturn(List.of(
                ResourceResponse.builder().name("VB-01").category(ResourceCategory.VEHICLE).status(ResourceStatus.AVAILABLE).build()
        ));

        mockMvc.perform(get("/resources").param("category", "VEHICLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void should_return_201_when_creating_resource() throws Exception {
        when(resourceService.createResource(any(ResourceRequest.class))).thenReturn(
                ResourceResponse.builder()
                        .id(1L)
                        .name("VB-03")
                        .description("Véhicule")
                        .category(ResourceCategory.VEHICLE)
                        .status(ResourceStatus.AVAILABLE)
                        .build()
        );

        mockMvc.perform(post("/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"VB-03\",\"description\":\"Véhicule\",\"category\":\"VEHICLE\"}"))
                        .andExpect(status().isCreated());
    }

    @Test
    public void should_return_200_when_getting_resource_by_id() throws Exception {
        when(resourceService.getResourceById(1L)).thenReturn(
                ResourceResponse.builder()
                        .id(1L)
                        .name("VB-01")
                        .category(ResourceCategory.VEHICLE)
                        .status(ResourceStatus.AVAILABLE)
                        .build()
        );

        mockMvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("VB-01"));
    }

    @Test
    public void should_return_200_when_updating_resource() throws Exception {
        when(resourceService.updateResource(eq(1L), any(ResourceRequest.class))).thenReturn(
                ResourceResponse.builder()
                        .id(1L)
                        .name("Véhicule VB-01")
                        .category(ResourceCategory.VEHICLE)
                        .status(ResourceStatus.AVAILABLE)
                        .build()
        );

        mockMvc.perform(put("/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Véhicule VB-01\",\"category\":\"VEHICLE\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Véhicule VB-01"));
    }

    @Test
    public void should_return_200_when_disabling_resource() throws Exception {
        when(resourceService.disableResource(eq(1L))).thenReturn(
                ResourceResponse.builder()
                        .id(1L)
                        .name("Véhicule VB-01")
                        .category(ResourceCategory.VEHICLE)
                        .status(ResourceStatus.DISABLED)
                        .build()
        );

        mockMvc.perform(delete("/resources/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value("DISABLED"));
    }


}
