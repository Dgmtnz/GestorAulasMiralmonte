package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joange.model.Usuario;
import com.joange.model.Reserva;
import com.joange.service.AulaService;
import com.joange.service.ReservaService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class HomeController {

    @Autowired
    private AulaService aulaService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/homeAdmin")
    public String adminHome(Model model, HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
        if (usuarioActual == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("todasReservas", reservaService.findAll());
        model.addAttribute("misReservas", 
            reservaService.findByUsuario(usuarioActual.getIdusuario()));
        
        return "home/adminHome";
    }
} 