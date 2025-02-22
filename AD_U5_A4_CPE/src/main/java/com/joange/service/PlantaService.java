package com.joange.service;

import java.util.List;

import com.joange.model.Planta;

public interface PlantaService {
    List<Planta> findAll();
    List<Planta> findActivos();
    List<Planta> findByEdificioIdedificio(Long idedificio);
    List<Planta> findByEdificioIdedificioAndActivoTrue(Long idedificio);
    Planta save(Planta planta);
    Planta findByIdplanta(Long idplanta);
    void deleteByIdplanta(Long idplanta);
} 