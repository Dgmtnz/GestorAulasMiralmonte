package com.joange.repository;
import com.joange.model.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdificioRepository extends JpaRepository<Edificio, Long> {
    List<Edificio> findByActivoTrue();
} 