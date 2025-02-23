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
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        
        Usuario usuarioActual = usuarioService.findByEmail(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        session.setAttribute("usuarioActual", usuarioActual);
        
        // Cargar todas las reservas
        List<Reserva> todasReservas = reservaService.findAll();
        model.addAttribute("todasReservas", todasReservas);
        
        // Cargar las reservas del usuario actual
        List<Reserva> misReservas = reservaService.findByUsuario(usuarioActual.getIdusuario());
        model.addAttribute("misReservas", misReservas);
        
        // Determinar qué vista mostrar basado en el rol del usuario
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return isAdmin ? "home/adminHome" : "home/userHome";
    }
} 