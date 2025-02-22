package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.joange.model.Curso;
import com.joange.repository.CursoRepository;
import com.joange.service.CursoService;

@Service
public class CursoServiceImpl implements CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Override
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }
    
    @Override
    public List<Curso> findActivos() {
        return cursoRepository.findByActivoTrue();
    }
    
    @Override
    public Curso findById(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }
    
    @Override
    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }
    
    @Override
    public void deleteById(Long id) {
        cursoRepository.deleteById(id);
    }
} 