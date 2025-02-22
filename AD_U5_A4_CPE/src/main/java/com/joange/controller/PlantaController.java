package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.joange.model.Planta;
import com.joange.service.EdificioService;
import com.joange.service.PlantaService;


@Controller
@RequestMapping("/plantas")
public class PlantaController {
    
    @Autowired
    private PlantaService plantaService;
    
    @Autowired
    private EdificioService edificioService;
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("planta", new Planta());
        model.addAttribute("edificios", edificioService.findActivos());
        return "plantas/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarPlanta(@ModelAttribute Planta planta, RedirectAttributes redirectAttributes) {
        try {
            planta.setIdplanta(null);  // Aseguramos que es una nueva planta
            if (planta.getActivo() == null) {
                planta.setActivo(true);
            }
            plantaService.save(planta);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Planta creada exitosamente!");
            return "redirect:/plantas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la planta: " + e.getMessage());
            return "redirect:/plantas/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarPlantas(Model model) {
        model.addAttribute("plantas", plantaService.findAll());
        return "plantas/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Planta planta = plantaService.findByIdplanta(id);
        if (planta == null) {
            return "redirect:/plantas/gestionar";
        }
        model.addAttribute("planta", planta);
        model.addAttribute("edificios", edificioService.findActivos());
        return "plantas/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPlanta(@PathVariable Long id, @ModelAttribute Planta planta, 
                                 RedirectAttributes redirectAttributes) {
        try {
            Planta plantaExistente = plantaService.findByIdplanta(id);
            if (plantaExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Planta no encontrada");
                return "redirect:/plantas/gestionar";
            }
            
            planta.setIdplanta(id);
            if (planta.getActivo() == null) {
                planta.setActivo(true);
            }
            
            plantaService.save(planta);
            redirectAttributes.addFlashAttribute("mensajeExito", "Planta actualizada exitosamente");
            return "redirect:/plantas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la planta: " + e.getMessage());
            return "redirect:/plantas/editar/" + id;
        }
    }

    @GetMapping("/ver/{id}")
    public String verPlanta(@PathVariable Long id, Model model) {
        Planta planta = plantaService.findByIdplanta(id);
        if (planta == null) {
            return "redirect:/plantas/gestionar";
        }
        model.addAttribute("planta", planta);
        return "plantas/ver";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPlanta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Planta planta = plantaService.findByIdplanta(id);
            if (planta == null) {
                redirectAttributes.addFlashAttribute("error", "Planta no encontrada");
                return "redirect:/plantas/gestionar";
            }

            // Verificar si tiene aulas asociadas
            if (planta.getAulas() != null && !planta.getAulas().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la planta porque tiene aulas asociadas");
                return "redirect:/plantas/gestionar";
            }

            plantaService.deleteByIdplanta(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Planta eliminada exitosamente");
            return "redirect:/plantas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la planta: " + e.getMessage());
            return "redirect:/plantas/gestionar";
        }
    }

    @GetMapping("/edificio/{idedificio}")
    @ResponseBody
    public List<Map<String, Object>> getPlantas(@PathVariable Long idedificio) {
        List<Planta> plantas = plantaService.findByEdificioIdedificioAndActivoTrue(idedificio);
        return plantas.stream()
            .map(p -> {
                Map<String, Object> plantaMap = new HashMap<>();
                plantaMap.put("idplanta", p.getIdplanta());
                plantaMap.put("nombre", p.getNombre());
                return plantaMap;
            })
            .collect(Collectors.toList());
    }
} 