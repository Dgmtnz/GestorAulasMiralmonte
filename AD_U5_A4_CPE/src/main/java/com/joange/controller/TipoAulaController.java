package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joange.model.TipoAula;
import com.joange.service.TipoAulaService;

@Controller
@RequestMapping("/tipoaulas")
public class TipoAulaController {
    
    @Autowired
    private TipoAulaService tipoAulaService;
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("tipoAula", new TipoAula());
        return "tipoaulas/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarTipoAula(@ModelAttribute TipoAula tipoAula, RedirectAttributes redirectAttributes) {
        try {
            tipoAulaService.save(tipoAula);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de aula creado exitosamente");
            return "redirect:/tipoaulas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el tipo de aula: " + e.getMessage());
            return "redirect:/tipoaulas/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarTiposAula(Model model) {
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "tipoaulas/listar";
    }
    
    @GetMapping("/editar/{id}")
    public String editarTipoAula(@PathVariable Long id, Model model) {
        TipoAula tipoAula = tipoAulaService.findById(id);
        if (tipoAula == null) {
            return "redirect:/tipoaulas/gestionar";
        }
        model.addAttribute("tipoAula", tipoAula);
        return "tipoaulas/editar";
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarTipoAula(@PathVariable Long id, @ModelAttribute TipoAula tipoAula, 
                                    RedirectAttributes redirectAttributes) {
        try {
            TipoAula existente = tipoAulaService.findById(id);
            if (existente == null) {
                redirectAttributes.addFlashAttribute("error", "Tipo de aula no encontrado");
                return "redirect:/tipoaulas/gestionar";
            }
            tipoAula.setIdtipo(id);
            tipoAulaService.save(tipoAula);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de aula actualizado exitosamente");
            return "redirect:/tipoaulas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el tipo de aula: " + e.getMessage());
            return "redirect:/tipoaulas/editar/" + id;
        }
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarTipoAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tipoAulaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Tipo de aula eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el tipo de aula: " + e.getMessage());
        }
        return "redirect:/tipoaulas/gestionar";
    }
} 