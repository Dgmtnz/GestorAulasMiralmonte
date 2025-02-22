package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.joange.model.TipoUsuario;
import com.joange.repository.TipoUsuarioRepository;
import com.joange.service.TipoUsuarioService;



@Service
public class TipoUsuarioServiceImpl implements TipoUsuarioService {
    
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    
    @Override
    public List<TipoUsuario> findAll() {
        return tipoUsuarioRepository.findAll();
    }
    
    @Override
    public TipoUsuario findById(Long id) {
        return tipoUsuarioRepository.findById(id).orElse(null);
    }
    
    @Override
    public TipoUsuario findByNombre(String nombre) {
        return tipoUsuarioRepository.findByNombre(nombre);
    }
    
    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        if (tipoUsuario.getActivo() == null) {
            tipoUsuario.setActivo(true);
        }
        return tipoUsuarioRepository.save(tipoUsuario);
    }
    
    @Override
    public void deleteById(Long id) {
        tipoUsuarioRepository.deleteById(id);
    }
    
    @Override
    public List<TipoUsuario> findActivos() {
        return tipoUsuarioRepository.findByActivoTrue();
    }
} 