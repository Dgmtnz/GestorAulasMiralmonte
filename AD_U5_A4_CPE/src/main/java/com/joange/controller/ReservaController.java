package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import com.joange.model.Aula;
import com.joange.model.Reserva;
import com.joange.model.Usuario;
import com.joange.model.Curso;
import com.joange.service.ReservaService;
import com.joange.service.AulaService;
import com.joange.service.UsuarioService;
import com.joange.service.CursoService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@Controller
@RequestMapping("/reservas")
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private AulaService aulaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CursoService cursoService;
    
    @GetMapping("/listar-aulas")
    public String listarAulasParaReserva(Model model) {
        List<Aula> aulas = aulaService.findAll();
        Map<Long, List<Reserva>> reservasPorAula = new HashMap<>();
        
        for (Aula aula : aulas) {
            reservasPorAula.put(aula.getIdaula(), 
                reservaService.findByAula(aula.getIdaula()));
        }
        
        model.addAttribute("aulas", aulas);
        model.addAttribute("reservasPorAula", reservasPorAula);
        return "reservas/listar-aulas";
    }
    
    @GetMapping("/crear/{idAula}")
    public String mostrarFormularioCreacion(@PathVariable Long idAula, Model model) {
        Aula aula = aulaService.findByIdaula(idAula);
        if (aula == null) {
            return "redirect:/reservas/listar-aulas";
        }
        
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setAula(aula);
        
        model.addAttribute("aula", aula);
        model.addAttribute("nuevaReserva", nuevaReserva);
        return "reservas/crear";
    }
    
    @GetMapping("/gestionar")
    public String gestionarReservas(Model model, HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
        if (usuarioActual == null) {
            return "redirect:/login";
        }
        
        List<Reserva> reservas = reservaService.findByUsuario(usuarioActual.getIdusuario());
        model.addAttribute("reservas", reservas);
        return "reservas/gestionar";
    }
    
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, 
                               @RequestParam(required = false) Long cursoId,
                               RedirectAttributes redirectAttributes, 
                               HttpSession session) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }

            // Configuración básica de la reserva
            reserva.setUsuario(usuarioActual);
            reserva.setValidar(false);
            reserva.setActivo(true);
            
            // Si se seleccionó un curso, asociarlo a la reserva
            if (cursoId != null) {
                Optional<Curso> curso = cursoService.findById(cursoId);
                if (curso.isPresent()) {
                    reserva.setCurso(curso.get());
                }
            }

            // Validaciones de disponibilidad y horarios
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reserva.getHoradesde());
            int horaInicio = calendar.get(Calendar.HOUR_OF_DAY);
            
            calendar.setTime(reserva.getHorahasta());
            int horaFin = calendar.get(Calendar.HOUR_OF_DAY);

            if (!reservaService.isAulaDisponible(
                    reserva.getAula().getIdaula(),
                    reserva.getFechadesde(),
                    horaInicio,
                    horaFin)) {
                redirectAttributes.addFlashAttribute("error", "El aula no está disponible en ese horario");
                return "redirect:/reservas/crear/" + reserva.getAula().getIdaula();
            }

            reserva.setFechahasta(reserva.getFechadesde());
            reservaService.save(reserva);
            
            String mensaje = reserva.getCurso() != null ? 
                "Reserva creada exitosamente para el curso " + reserva.getCurso().getNombre() :
                "Reserva personal creada exitosamente";
            
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
            return "redirect:/homeAdmin";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la reserva: " + e.getMessage());
            return "redirect:/reservas/listar-aulas";
        }
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, 
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            Optional<Reserva> reserva = reservaService.findById(id);
            if (!reserva.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/homeAdmin";
            }
            
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (!reserva.get().getUsuario().getIdusuario().equals(usuarioActual.getIdusuario())) {
                redirectAttributes.addFlashAttribute("error", 
                    "No tienes permiso para eliminar esta reserva");
                return "redirect:/homeAdmin";
            }
            
            reservaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva eliminada exitosamente");
            return "redirect:/homeAdmin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar la reserva: " + e.getMessage());
            return "redirect:/homeAdmin";
        }
    }

    @GetMapping("/api/reservas/disponibilidad/{aulaId}/{fecha}")
    @ResponseBody
    public List<Integer> getHorasOcupadas(@PathVariable Long aulaId, @PathVariable String fecha) {
        try {
            // Convertir String fecha a Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = formatter.parse(fecha);
            
            List<Reserva> reservas = reservaService.findByFechaAndAula(fechaDate, aulaId);
            List<Integer> horasOcupadas = new ArrayList<>();
            
            for (Reserva reserva : reservas) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(reserva.getHoradesde());
                int inicio = cal.get(Calendar.HOUR_OF_DAY);
                cal.setTime(reserva.getHorahasta());
                int fin = cal.get(Calendar.HOUR_OF_DAY);
                
                for (int hora = inicio; hora < fin; hora++) {
                    horasOcupadas.add(hora);
                }
            }
            
            return horasOcupadas;
        } catch (ParseException e) {
            return new ArrayList<>();
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        dateFormat.setLenient(false);
        timeFormat.setLenient(false);
        
        binder.registerCustomEditor(Date.class, "fechadesde", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "fechahasta", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "horadesde", new CustomDateEditor(timeFormat, true));
        binder.registerCustomEditor(Date.class, "horahasta", new CustomDateEditor(timeFormat, true));
    }
} 