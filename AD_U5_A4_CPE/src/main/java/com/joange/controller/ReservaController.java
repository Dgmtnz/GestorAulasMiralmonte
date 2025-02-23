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
import org.springframework.transaction.annotation.Transactional;

import com.joange.model.Aula;
import com.joange.model.Reserva;
import com.joange.model.Usuario;
import com.joange.model.Curso;
import com.joange.service.ReservaService;
import com.joange.service.AulaService;
import com.joange.service.UsuarioService;
import com.joange.service.CursoService;
import com.joange.service.EdificioService;
import com.joange.service.TipoAulaService;

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
    
    @Autowired
    private EdificioService edificioService;
    
    @Autowired
    private TipoAulaService tipoAulaService;
    
    @GetMapping("/listar-aulas")
    public String listarAulasParaReserva(Model model) {
        model.addAttribute("aulas", aulaService.findAll());
        model.addAttribute("edificios", edificioService.findActivos());
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "reservas/listar-aulas";
    }
    
    @GetMapping("/crear/{idAula}")
    public String mostrarFormularioCreacion(@PathVariable Long idAula, Model model, HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        Aula aula = aulaService.findByIdaula(idAula);
        if (aula == null) {
            return "redirect:/reservas/listar-aulas";
        }
        
        List<Curso> cursos = usuarioService.findCursosByUsuarioId(usuarioActual.getIdusuario());
        
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setAula(aula);
        nuevaReserva.setUsuario(usuarioActual);
        
        model.addAttribute("aula", aula);
        model.addAttribute("nuevaReserva", nuevaReserva);
        model.addAttribute("cursos", cursos);
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
    @Transactional
    public String guardarReserva(@ModelAttribute Reserva nuevaReserva, 
                               @RequestParam(required = false) Long cursoId,
                               @RequestParam String tiporeserva,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }

            nuevaReserva.setUsuario(usuarioActual);
            
            // Si es una reserva de curso, establecer el curso
            if ("curso".equals(tiporeserva) && cursoId != null) {
                Curso curso = cursoService.findById(cursoId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
                nuevaReserva.setCurso(curso);
            }
            
            reservaService.save(nuevaReserva);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva creada con éxito");
            return "redirect:/home";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la reserva: " + e.getMessage());
            return "redirect:/reservas/listar-aulas";
        }
    }
    
    @PostMapping("/eliminar/{id}")
    @Transactional
    public String eliminarReserva(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }
    
            Optional<Reserva> reservaOpt = reservaService.findById(id);
            if (!reservaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/home";
            }
    
            Reserva reserva = reservaOpt.get();
            boolean esAdmin = usuarioService.esAdministrador(usuarioActual);
            
            // Si es admin, puede eliminar cualquier reserva
            if (esAdmin) {
                reservaService.deleteById(id);
                redirectAttributes.addFlashAttribute("mensajeExito", "Reserva eliminada correctamente");
                return "redirect:/home";
            }
            
            // Si no es admin, solo puede eliminar sus propias reservas
            if (!reserva.getUsuario().getIdusuario().equals(usuarioActual.getIdusuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta reserva");
                return "redirect:/home";
            }
            
            reservaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva eliminada correctamente");
            return "redirect:/home";
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @GetMapping("/api/reservas/disponibilidad/{aulaId}/{fecha}")
    @ResponseBody
    public List<Integer> getHorasOcupadas(@PathVariable Long aulaId, @PathVariable String fecha,
                                         @RequestParam(required = false) Long excludeReservaId) {
        try {
            // Convertir String fecha a Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = formatter.parse(fecha);
            
            List<Reserva> reservas = reservaService.findByFechaAndAula(fechaDate, aulaId);
            List<Integer> horasOcupadas = new ArrayList<>();
            
            for (Reserva reserva : reservas) {
                // Excluir la reserva que se está editando
                if (excludeReservaId != null && reserva.getIdreserva().equals(excludeReservaId)) {
                    continue;
                }
                
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

    @GetMapping("/calendario")
    public String mostrarCalendarioGeneral(Model model, HttpSession session) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }

            List<Reserva> reservas = reservaService.findAll();
            List<Map<String, Object>> eventosCalendario = new ArrayList<>();

            for (Reserva reserva : reservas) {
                if (reserva.getFechadesde() != null && reserva.getHoradesde() != null && reserva.getHorahasta() != null) {
                    Map<String, Object> evento = new HashMap<>();
                    evento.put("title", reserva.getAula().getNombre() + " - " + reserva.getUsuario().getNombre());
                    
                    Calendar calInicio = Calendar.getInstance();
                    calInicio.setTime(reserva.getFechadesde());
                    Calendar horaInicio = Calendar.getInstance();
                    horaInicio.setTime(reserva.getHoradesde());
                    calInicio.set(Calendar.HOUR_OF_DAY, horaInicio.get(Calendar.HOUR_OF_DAY));
                    calInicio.set(Calendar.MINUTE, horaInicio.get(Calendar.MINUTE));
                    evento.put("start", calInicio.getTime());
                    
                    Calendar calFin = Calendar.getInstance();
                    calFin.setTime(reserva.getFechadesde());
                    Calendar horaFin = Calendar.getInstance();
                    horaFin.setTime(reserva.getHorahasta());
                    calFin.set(Calendar.HOUR_OF_DAY, horaFin.get(Calendar.HOUR_OF_DAY));
                    calFin.set(Calendar.MINUTE, horaFin.get(Calendar.MINUTE));
                    evento.put("end", calFin.getTime());
                    
                    eventosCalendario.add(evento);
                }
            }
            
            model.addAttribute("eventos", eventosCalendario);
            return "reservas/calendario";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/editar/{id}")
    @Transactional
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }

            Optional<Reserva> reservaOpt = reservaService.findById(id);
            if (!reservaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/home";
            }

            Reserva reserva = reservaOpt.get();
            boolean esAdmin = usuarioService.esAdministrador(usuarioActual);
            
            if (!esAdmin && !reserva.getUsuario().getIdusuario().equals(usuarioActual.getIdusuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta reserva");
                return "redirect:/home";
            }

            model.addAttribute("reserva", reserva);
            model.addAttribute("aula", reserva.getAula());
            return "reservas/editar";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cargar la reserva: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @PostMapping("/actualizar/{id}")
    @Transactional
    public String actualizarReserva(@PathVariable Long id, 
                                   @ModelAttribute Reserva reserva,
                                   HttpSession session, 
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
            if (usuarioActual == null) {
                return "redirect:/login";
            }

            Optional<Reserva> reservaExistenteOpt = reservaService.findById(id);
            if (!reservaExistenteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/home";
            }

            Reserva reservaExistente = reservaExistenteOpt.get();
            boolean esAdmin = usuarioService.esAdministrador(usuarioActual);
            
            // Si no es admin, verificar que sea el propietario de la reserva
            if (!esAdmin && !reservaExistente.getUsuario().getIdusuario().equals(usuarioActual.getIdusuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta reserva");
                return "redirect:/home";
            }

            // Mantener los datos que no deben cambiar
            reserva.setIdreserva(id);
            reserva.setUsuario(reservaExistente.getUsuario());
            reserva.setAula(reservaExistente.getAula());
            reserva.setActivo(true);
            reserva.setValidar(reservaExistente.getValidar());
            reserva.setFechahasta(reserva.getFechadesde());
            reserva.setCurso(reservaExistente.getCurso());

            // Validar disponibilidad
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reserva.getHoradesde());
            int horaInicio = calendar.get(Calendar.HOUR_OF_DAY);
            
            calendar.setTime(reserva.getHorahasta());
            int horaFin = calendar.get(Calendar.HOUR_OF_DAY);

            // Verificar disponibilidad excluyendo la reserva actual
            if (!reservaService.isAulaDisponible(
                    reserva.getAula().getIdaula(),
                    reserva.getFechadesde(),
                    horaInicio,
                    horaFin,
                    id)) {
                redirectAttributes.addFlashAttribute("error", "El aula no está disponible en ese horario");
                return "redirect:/reservas/editar/" + id;
            }

            reservaService.save(reserva);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva actualizada correctamente");
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la reserva: " + e.getMessage());
            return "redirect:/reservas/editar/" + id;
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        dateFormat.setLenient(false);
        timeFormat.setLenient(false);
        
        binder.registerCustomEditor(Date.class, "fechadesde", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "fechahasta", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "horadesde", new CustomDateEditor(timeFormat, true));
        binder.registerCustomEditor(Date.class, "horahasta", new CustomDateEditor(timeFormat, true));
    }
} 