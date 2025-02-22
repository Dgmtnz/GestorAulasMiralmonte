package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.joange.model.FamCurso;
import com.joange.repository.FamCursoRepository;
import com.joange.service.FamCursoService;

@Service
public class FamCursoServiceImpl implements FamCursoService {
    
    @Autowired
    private FamCursoRepository famCursoRepository;
    
    @Override
    public List<FamCurso> findAll() {
        return famCursoRepository.findAll();
    }
    
    @Override
    public List<FamCurso> findActivos() {
        return famCursoRepository.findByActivoTrue();
    }
    
    @Override
    public FamCurso findById(Long id) {
        return famCursoRepository.findById(id).orElse(null);
    }
    
    @Override
    public FamCurso save(FamCurso famCurso) {
        return famCursoRepository.save(famCurso);
    }
    
    @Override
    public void deleteById(Long id) {
        famCursoRepository.deleteById(id);
    }
} 