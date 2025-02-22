package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joange.model.TipoUsuario;
import com.joange.service.TipoUsuarioService;

@Controller
@RequestMapping("/tipousuarios")
public class TipoUsuarioController {
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("tipoUsuario", new TipoUsuario());
        return "tipousuarios/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarTipoUsuario(@ModelAttribute TipoUsuario tipoUsuario, 
                                    RedirectAttributes redirectAttributes) {
        try {
            tipoUsuarioService.save(tipoUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de usuario creado exitosamente");
            return "redirect:/homeAdmin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el tipo de usuario: " + e.getMessage());
            return "redirect:/tipousuarios/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarTiposUsuario(Model model) {
        model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
        return "tipousuarios/listar";
    }
} 