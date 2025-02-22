package com.joange.service;

import java.util.List;

import com.joange.model.FamCurso;

public interface FamCursoService {
    List<FamCurso> findAll();
    List<FamCurso> findActivos();
    FamCurso findById(Long id);
    FamCurso save(FamCurso famCurso);
    void deleteById(Long id);
} 