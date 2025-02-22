package com.joange.service;
import java.util.List;
import java.util.Optional;

import com.joange.model.Usuario;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findByIdusuario(Long idusuario);
    Optional<Usuario> findByEmail(String email);
    Usuario findByDni(String dni);
    Usuario save(Usuario usuario);
    void deleteByIdusuario(Long idusuario);
    List<Usuario> findActivos();
    boolean autenticar(String email, String contrasenya);
    boolean esAdministrador(Usuario usuario);
} 