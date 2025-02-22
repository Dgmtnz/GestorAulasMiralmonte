package com.joange.service;

import java.util.List;

import com.joange.model.Curso;

public interface CursoService {
    List<Curso> findAll();
    List<Curso> findActivos();
    Curso findById(Long id);
    Curso save(Curso curso);
    void deleteById(Long id);
} 