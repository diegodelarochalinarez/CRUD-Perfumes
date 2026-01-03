package com.diego.crud.mapper;

import com.diego.crud.dto.PerfumeDTO;
import com.diego.crud.model.Perfume;
import org.springframework.stereotype.Component;

@Component
public class PerfumeMapper {

    public PerfumeDTO toDTO(Perfume perfume) {
        if (perfume == null) {
            return null;
        }
        return new PerfumeDTO(
                perfume.getId(),
                perfume.getNombre(),
                perfume.getMarca(),
                perfume.getPrecio(),
                perfume.getStock()
        );
    }

    public Perfume toEntity(PerfumeDTO dto) {
        if (dto == null) {
            return null;
        }
        Perfume perfume = new Perfume();
        perfume.setId(dto.getId());
        perfume.setNombre(dto.getNombre());
        perfume.setMarca(dto.getMarca());
        perfume.setPrecio(dto.getPrecio());
        perfume.setStock(dto.getStock());
        return perfume;
    }
}
