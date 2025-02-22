package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.joange.model.Planta;
import com.joange.repository.PlantaRepository;
import com.joange.service.PlantaService;

@Service
public class PlantaServiceImpl implements PlantaService {
    
    @Autowired
    private PlantaRepository plantaRepository;
    
    @Override
    public List<Planta> findAll() {
        return plantaRepository.findAll();
    }
    
    @Override
    public List<Planta> findActivos() {
        return plantaRepository.findByActivoTrue();
    }
    
    @Override
    public List<Planta> findByEdificioIdedificio(Long idedificio) {
        return plantaRepository.findByEdificioIdedificio(idedificio);
    }
    
    @Override
    public List<Planta> findByEdificioIdedificioAndActivoTrue(Long idedificio) {
        return plantaRepository.findByEdificioIdedificioAndActivoTrue(idedificio);
    }
    
    @Override
    public Planta save(Planta planta) {
        return plantaRepository.save(planta);
    }
    
    @Override
    public Planta findByIdplanta(Long idplanta) {
        return plantaRepository.findById(idplanta).orElse(null);
    }
    
    @Override
    public void deleteByIdplanta(Long idplanta) {
        plantaRepository.deleteById(idplanta);
    }
} 