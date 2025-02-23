package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joange.model.Usuario;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Usuario findByDni(String dni);
    List<Usuario> findByActivoTrue();
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
} 