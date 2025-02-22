package com.joange.serviceImpl;

import com.joange.model.Edificio;
import com.joange.repository.EdificioRepository;
import com.joange.service.EdificioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdificioServiceImpl implements EdificioService {
    
    @Autowired
    private EdificioRepository edificioRepository;
    
    @Override
    public List<Edificio> findAll() {
        return edificioRepository.findAll();
    }
    
    @Override
    public List<Edificio> findActivos() {
        return edificioRepository.findByActivoTrue();
    }
    
    @Override
    public Edificio save(Edificio edificio) {
        return edificioRepository.save(edificio);
    }
    
    @Override
    public Edificio findById(Long id) {
        return edificioRepository.findById(id).orElse(null);
    }
    
    @Override
    public void deleteById(Long id) {
        edificioRepository.deleteById(id);
    }
} 