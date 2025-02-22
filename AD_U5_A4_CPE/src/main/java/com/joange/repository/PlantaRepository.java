package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.joange.model.Planta;

@Repository
public interface PlantaRepository extends JpaRepository<Planta, Long> {
    List<Planta> findByActivoTrue();
    List<Planta> findByEdificioIdedificio(Long idedificio);
    List<Planta> findByEdificioIdedificioAndActivoTrue(Long idedificio);
} 