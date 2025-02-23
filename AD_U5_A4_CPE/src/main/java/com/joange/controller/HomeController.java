package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.joange.model.Usuario;
import com.joange.model.Reserva;
import com.joange.model.Curso;
import com.joange.service.AulaService;
import com.joange.service.ReservaService;
import com.joange.service.UsuarioService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AulaService aulaService;

    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/", "/home", "/homeAdmin"})
    public String home(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if user is authenticated and it's not the anonymous user
        if (auth == null || !auth.isAuthenticated() || 
            "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        
        try {
            Usuario usuarioActual = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            session.setAttribute("usuarioActual", usuarioActual);
            
            // Obtener todas las reservas
            List<Reserva> todasReservas = reservaService.findAll();
            
            // Obtener reservas personales (donde curso es null)
            List<Reserva> reservasPersonales = reservaService.findByUsuarioAndCursoIsNull(usuarioActual);
            
            // Obtener los cursos del usuario
            List<Curso> cursosUsuario = usuarioService.findCursosByUsuarioId(usuarioActual.getIdusuario());
            
            // Obtener todas las reservas de los cursos a los que pertenece el usuario
            List<Reserva> reservasCursos = reservaService.findByCursoInAndCursoIsNotNull(cursosUsuario);
            
            model.addAttribute("todasReservas", todasReservas);
            model.addAttribute("reservasPersonales", reservasPersonales);
            model.addAttribute("reservasCursos", reservasCursos);
            
            // Determinar qué vista mostrar basado en el rol del usuario
            boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return isAdmin ? "home/adminHome" : "home/userHome";
            
        } catch (Exception e) {
            // Log the error and redirect to login
            return "redirect:/login";
        }
    }
} 