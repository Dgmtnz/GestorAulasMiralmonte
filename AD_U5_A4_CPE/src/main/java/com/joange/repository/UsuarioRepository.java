package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.joange.model.Curso;
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
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.usuarioCursos uc LEFT JOIN FETCH uc.curso WHERE u.idusuario = :id")
    Usuario findByIdWithCursos(@Param("id") Long id);
    @Query("SELECT uc.curso FROM UsuarioCurso uc WHERE uc.usuario.idusuario = :usuarioId")
    List<Curso> findCursosByUsuarioId(@Param("usuarioId") Long usuarioId);
} 