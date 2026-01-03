package com.diego.crud.repository;

import com.diego.crud.model.Perfume;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Perfume p WHERE p.id = :id")
    Optional<Perfume> findByIdWithLock(Long id);
}
