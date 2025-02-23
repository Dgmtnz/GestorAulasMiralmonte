package com.joange.service;
import java.util.List;
import java.util.Optional;

import com.joange.model.Usuario;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Usuario save(Usuario usuario);
    void deleteById(Long id);
    List<Usuario> findActivos();
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Usuario findByDni(String dni);
    boolean autenticar(String email, String contrasenya);
    boolean esAdministrador(Usuario usuario);
} 