package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joange.model.Reserva;
import com.joange.repository.ReservaRepository;
import com.joange.service.ReservaService;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.Calendar;

@Service
public class ReservaServiceImpl implements ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Override
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }
    
    @Override
    public Optional<Reserva> findById(Long idreserva) {
        return reservaRepository.findById(idreserva);
    }
    
    @Override
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
    
    @Override
    public void deleteById(Long idreserva) {
        reservaRepository.deleteById(idreserva);
    }
    
    @Override
    public List<Reserva> findByFecha(Date fecha) {
        return reservaRepository.findByFechadesde(fecha);
    }
    
    @Override
    public List<Reserva> findByUsuario(Long idusuario) {
        return reservaRepository.findByUsuarioIdusuario(idusuario);
    }
    
    @Override
    public List<Reserva> findByAula(Long idaula) {
        return reservaRepository.findByAulaIdaula(idaula);
    }
    
    @Override
    public List<Reserva> findByFechaAndAula(Date fecha, Long idaula) {
        return reservaRepository.findByFechadesdeAndAulaIdaula(fecha, idaula);
    }
    
    @Override
    public boolean isAulaDisponible(Long idaula, Date fecha, Integer horaInicio, Integer horaFin) {
        return isAulaDisponible(idaula, fecha, horaInicio, horaFin, null);
    }

    @Override
    public boolean isAulaDisponible(Long idaula, Date fecha, Integer horaInicio, Integer horaFin, Long idReservaExcluir) {
        List<Reserva> reservasExistentes = findByFechaAndAula(fecha, idaula);
        Calendar calendar = Calendar.getInstance();
        
        for (Reserva reserva : reservasExistentes) {
            // Si es la reserva que estamos editando, la ignoramos
            if (idReservaExcluir != null && reserva.getIdreserva().equals(idReservaExcluir)) {
                continue;
            }

            calendar.setTime(reserva.getHoradesde());
            int horaReservaInicio = calendar.get(Calendar.HOUR_OF_DAY);
            
            calendar.setTime(reserva.getHorahasta());
            int horaReservaFin = calendar.get(Calendar.HOUR_OF_DAY);
            
            // Verificar si hay solapamiento de horarios
            if (!(horaFin <= horaReservaInicio || horaInicio >= horaReservaFin)) {
                return false;
            }
        }
        return true;
    }
} 