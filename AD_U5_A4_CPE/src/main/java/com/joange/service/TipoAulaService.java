package com.joange.service;

import java.util.List;

import com.joange.model.TipoAula;

public interface TipoAulaService {
    List<TipoAula> findAll();
    List<TipoAula> findActivos();
    TipoAula save(TipoAula tipoAula);
    TipoAula findById(Long id);
    void deleteById(Long id);
} 