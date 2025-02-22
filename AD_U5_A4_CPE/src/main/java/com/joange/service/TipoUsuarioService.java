package com.joange.service;

import com.joange.model.TipoUsuario;
import java.util.List;
import java.util.Optional;


public interface TipoUsuarioService {
    List<TipoUsuario> findAll();
    TipoUsuario findById(Long id);
    TipoUsuario findByNombre(String nombre);
    TipoUsuario save(TipoUsuario tipoUsuario);
    void deleteById(Long id);
    List<TipoUsuario> findActivos();
} 