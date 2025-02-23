package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.joange.model.Reserva;
import java.util.Date;
import java.util.List;
import com.joange.model.Usuario;
import com.joange.model.Curso;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFechadesde(Date fechadesde);
    List<Reserva> findByUsuarioIdusuario(Long idusuario);
    List<Reserva> findByAulaIdaula(Long idaula);
    List<Reserva> findByFechadesdeAndAulaIdaula(Date fechadesde, Long idaula);
    List<Reserva> findByUsuarioAndCursoIsNull(Usuario usuario);
    List<Reserva> findByUsuarioAndCursoIsNotNull(Usuario usuario);
    List<Reserva> findByCursoInAndCursoIsNotNull(List<Curso> cursos);
} 