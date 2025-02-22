package com.joange.service;

import java.util.List;

import com.joange.model.Edificio;

public interface EdificioService {
    List<Edificio> findAll();
    List<Edificio> findActivos();
    Edificio save(Edificio edificio);
    Edificio findById(Long id);
    void deleteById(Long id);
} 