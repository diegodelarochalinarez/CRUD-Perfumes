package com.diego.crud.controller;

import com.diego.crud.dto.PerfumeDTO;
import com.diego.crud.service.PerfumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfumes")
public class PerfumesController {

    private final PerfumeService perfumeService;

    @Autowired
    public PerfumesController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    public List<PerfumeDTO> getAllPerfumes() {
        return perfumeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfumeDTO> getPerfumeById(@PathVariable Long id) {
        PerfumeDTO perfume = perfumeService.findById(id);
        if (perfume != null) {
            return ResponseEntity.ok(perfume);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public PerfumeDTO createPerfume(@RequestBody PerfumeDTO perfumeDTO) {
        return perfumeService.save(perfumeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfumeDTO> updatePerfume(@PathVariable Long id, @RequestBody PerfumeDTO perfumeDetails) {
        PerfumeDTO updatedPerfume = perfumeService.update(id, perfumeDetails);

        if (updatedPerfume != null) {
            return ResponseEntity.ok(updatedPerfume);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        perfumeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
