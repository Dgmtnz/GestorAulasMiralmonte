package com.joange.service;

import com.joange.model.Reserva;
import com.joange.model.Usuario;
import com.joange.model.Curso;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public interface ReservaService {
   List<Reserva> findAll();
    Optional<Reserva> findById(Long idreserva);
    Reserva save(Reserva reserva);
    void deleteById(Long idreserva);
    List<Reserva> findByFecha(Date fecha);
    List<Reserva> findByUsuario(Long idusuario);
    List<Reserva> findByAula(Long idaula);
    boolean isAulaDisponible(Long idaula, Date fecha, Integer horaInicio, Integer horaFin);
    boolean isAulaDisponible(Long idaula, Date fecha, Integer horaInicio, Integer horaFin, Long idReservaExcluir);
    List<Reserva> findByUsuarioAndCursoIsNull(Usuario usuario);
    List<Reserva> findByUsuarioAndCursoIsNotNull(Usuario usuario);
    List<Reserva> findByCursoInAndCursoIsNotNull(List<Curso> cursos);
    List<Reserva> findByFechaAndAula(Date fecha, Long idaula);
} 