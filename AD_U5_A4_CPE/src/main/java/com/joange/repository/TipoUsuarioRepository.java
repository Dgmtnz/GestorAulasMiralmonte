package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joange.model.TipoUsuario;
import java.util.List;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    TipoUsuario findByNombre(String nombre);
    List<TipoUsuario> findByActivoTrue();
}