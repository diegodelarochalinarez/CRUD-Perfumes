package com.diego.crud.controller;

import com.diego.crud.config.JwtAuthenticationFilter;
import com.diego.crud.dto.PerfumeDTO;
import com.diego.crud.service.PerfumeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@WebMvcTest(PerfumesController.class)
public class PerfumesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfumeService perfumeService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private PerfumeDTO perfumeDTO;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        perfumeDTO = new PerfumeDTO();
        perfumeDTO.setId(1L);
        perfumeDTO.setNombre("Test Perfume");
        perfumeDTO.setMarca("Test Brand");
        perfumeDTO.setPrecio(BigDecimal.valueOf(100.0));
        perfumeDTO.setStock(10);

        doAnswer(invocation -> {
            ServletRequest request = invocation.getArgument(0);
            ServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getAllPerfumes_ShouldReturnList() throws Exception {
        List<PerfumeDTO> perfumes = Arrays.asList(perfumeDTO);
        given(perfumeService.findAll()).willReturn(perfumes);

        mockMvc.perform(get("/api/perfumes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Test Perfume"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getPerfumeById_ShouldReturnPerfume() throws Exception {
        given(perfumeService.findById(1L)).willReturn(perfumeDTO);

        mockMvc.perform(get("/api/perfumes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Perfume"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createPerfume_ShouldReturnCreated() throws Exception {
        given(perfumeService.save(any(PerfumeDTO.class))).willReturn(perfumeDTO);

        mockMvc.perform(post("/api/perfumes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfumeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Perfume"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void updatePerfume_ShouldReturnUpdated() throws Exception {
        given(perfumeService.update(eq(1L), any(PerfumeDTO.class))).willReturn(perfumeDTO);

        mockMvc.perform(put("/api/perfumes/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfumeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Perfume"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void deletePerfume_ShouldReturnNoContent() throws Exception {
        doNothing().when(perfumeService).deleteById(1L);

        mockMvc.perform(delete("/api/perfumes/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
