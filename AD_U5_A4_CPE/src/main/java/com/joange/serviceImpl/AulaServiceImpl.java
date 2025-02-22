package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.joange.model.Aula;
import com.joange.repository.AulaRepository;
import com.joange.service.AulaService;

@Service
public class AulaServiceImpl implements AulaService {
    
    @Autowired
    private AulaRepository aulaRepository;
    
    @Override
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }
    
    @Override
    public List<Aula> findActivos() {
        return aulaRepository.findByActivoTrue();
    }
    
    @Override
    public List<Aula> findByPlantaIdplanta(Long idplanta) {
        return aulaRepository.findByPlantaIdplanta(idplanta);
    }
    
    @Override
    public List<Aula> findActivosByPlanta(Long idplanta) {
        return aulaRepository.findByPlantaIdplantaAndActivoTrue(idplanta);
    }
    
    @Override
    public Aula save(Aula aula) {
        return aulaRepository.save(aula);
    }
    
    @Override
    public Aula findByIdaula(Long idaula) {
        return aulaRepository.findById(idaula).orElse(null);
    }
    
    @Override
    public void deleteByIdaula(Long idaula) {
        aulaRepository.deleteById(idaula);
    }
}