package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joange.model.Curso;
import com.joange.service.CursoService;
import com.joange.service.FamCursoService;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.ArrayList;


@Controller
@RequestMapping("/cursos")
public class CursoController {
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private FamCursoService famCursoService;
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("familiasCurso", famCursoService.findActivos());
        return "cursos/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarCurso(@ModelAttribute Curso curso, 
                            RedirectAttributes redirectAttributes) {
        try {
            curso.setIdcurso(null);
            
            if (curso.getActivo() == null) {
                curso.setActivo(true);
            }
            
            cursoService.save(curso);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Curso creado exitosamente!");
            return "redirect:/homeAdmin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el curso: " + e.getMessage());
            return "redirect:/cursos/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoService.findAll());
        return "cursos/listar";
    }
} 