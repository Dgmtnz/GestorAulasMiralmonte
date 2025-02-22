package com.joange.service;

import com.joange.model.Aula;
import java.util.List;

public interface AulaService {
    List<Aula> findAll();
    List<Aula> findActivos();
    List<Aula> findByPlantaIdplanta(Long idplanta);
    List<Aula> findActivosByPlanta(Long idplanta);
    Aula save(Aula aula);
    Aula findByIdaula(Long idaula);
    void deleteByIdaula(Long idaula);
}