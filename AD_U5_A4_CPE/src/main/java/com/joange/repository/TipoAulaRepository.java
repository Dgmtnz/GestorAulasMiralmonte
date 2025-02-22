package com.joange.repository;

import com.joange.model.TipoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoAulaRepository extends JpaRepository<TipoAula, Long> {
    List<TipoAula> findByActivoTrue();
} 