package com.diego.crud.service;

import com.diego.crud.dto.PerfumeDTO;
import com.diego.crud.mapper.PerfumeMapper;
import com.diego.crud.model.Perfume;
import com.diego.crud.repository.PerfumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private PerfumeMapper perfumeMapper;

    @InjectMocks
    private PerfumeService perfumeService;

    private Perfume perfume;
    private PerfumeDTO perfumeDTO;

    @BeforeEach
    void setUp() {
        perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Test Perfume");
        perfume.setMarca("Test Brand");
        perfume.setPrecio(new BigDecimal(100));
        perfume.setStock(10);

        perfumeDTO = new PerfumeDTO();
        perfumeDTO.setId(1L);
        perfumeDTO.setNombre("Test Perfume");
        perfumeDTO.setMarca("Test Brand");
        perfumeDTO.setPrecio(new BigDecimal(100.0));
        perfumeDTO.setStock(10);
    }

    @Test
    void findAll_ShouldReturnListOfPerfumeDTOs() {
        when(perfumeRepository.findAll()).thenReturn(Arrays.asList(perfume));
        when(perfumeMapper.toDTO(perfume)).thenReturn(perfumeDTO);

        List<PerfumeDTO> result = perfumeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(perfumeDTO.getNombre(), result.get(0).getNombre());
        verify(perfumeRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnPerfumeDTO() {
        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
        when(perfumeMapper.toDTO(perfume)).thenReturn(perfumeDTO);

        PerfumeDTO result = perfumeService.findById(1L);

        assertNotNull(result);
        assertEquals(perfumeDTO.getId(), result.getId());
        verify(perfumeRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnNull() {
        when(perfumeRepository.findById(99L)).thenReturn(Optional.empty());

        PerfumeDTO result = perfumeService.findById(99L);

        assertNull(result);
        verify(perfumeRepository, times(1)).findById(99L);
    }

    @Test
    void save_ShouldReturnSavedPerfumeDTO() {
        when(perfumeMapper.toEntity(perfumeDTO)).thenReturn(perfume);
        when(perfumeRepository.save(perfume)).thenReturn(perfume);
        when(perfumeMapper.toDTO(perfume)).thenReturn(perfumeDTO);

        PerfumeDTO result = perfumeService.save(perfumeDTO);

        assertNotNull(result);
        assertEquals(perfumeDTO.getNombre(), result.getNombre());
        verify(perfumeRepository, times(1)).save(perfume);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(perfumeRepository).deleteById(1L);

        perfumeService.deleteById(1L);

        verify(perfumeRepository, times(1)).deleteById(1L);
    }
}
