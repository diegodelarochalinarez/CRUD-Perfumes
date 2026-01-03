package com.diego.crud.service;

import com.diego.crud.dto.PerfumeDTO;
import com.diego.crud.mapper.PerfumeMapper;
import com.diego.crud.model.Perfume;
import com.diego.crud.repository.PerfumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final PerfumeMapper perfumeMapper;

    @Autowired
    public PerfumeService(PerfumeRepository perfumeRepository, PerfumeMapper perfumeMapper) {
        this.perfumeRepository = perfumeRepository;
        this.perfumeMapper = perfumeMapper;
    }

    @Cacheable("perfumes")
    public List<PerfumeDTO> findAll() {
        return perfumeRepository.findAll().stream()
                .map(perfumeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "perfumes", key = "#id")
    public PerfumeDTO findById(Long id) {
        return perfumeRepository.findById(id)
                .map(perfumeMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = "perfumes", allEntries = true)
    public PerfumeDTO update(Long id, PerfumeDTO perfumeDetails) {
        Optional<Perfume> existingPerfume = perfumeRepository.findByIdWithLock(id);
        if (existingPerfume.isEmpty()) {
            return null;
        }
        perfumeDetails.setId(id);
        return this.save(perfumeDetails);
    }

    @CacheEvict(value = "perfumes", allEntries = true)
    public PerfumeDTO save(PerfumeDTO perfumeDTO) {
        Perfume perfume = perfumeMapper.toEntity(perfumeDTO);
        Perfume savedPerfume = perfumeRepository.save(perfume);
        return perfumeMapper.toDTO(savedPerfume);
    }

    @CacheEvict(value = "perfumes", allEntries = true)
    public void deleteById(Long id) {
        perfumeRepository.deleteById(id);
    }
}
