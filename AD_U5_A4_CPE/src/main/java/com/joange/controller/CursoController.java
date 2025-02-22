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
            return "redirect:/cursos/gestionar";
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

    @GetMapping("/ver/{id}")
    public String verCurso(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Curso> cursoOpt = cursoService.findById(id);
            if (cursoOpt.isPresent()) {
                model.addAttribute("curso", cursoOpt.get());
                return "cursos/ver";
            } else {
                redirectAttributes.addFlashAttribute("error", "El curso no existe");
                return "redirect:/cursos/gestionar";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el curso: " + e.getMessage());
            return "redirect:/cursos/gestionar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Curso> cursoOpt = cursoService.findById(id);
            if (cursoOpt.isPresent()) {
                model.addAttribute("curso", cursoOpt.get());
                model.addAttribute("familiasCurso", famCursoService.findActivos());
                return "cursos/editar";
            } else {
                redirectAttributes.addFlashAttribute("error", "El curso no existe");
                return "redirect:/cursos/gestionar";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el curso: " + e.getMessage());
            return "redirect:/cursos/gestionar";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCurso(@PathVariable Long id, @ModelAttribute Curso curso, 
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Curso> cursoExistente = cursoService.findById(id);
            if (!cursoExistente.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El curso no existe");
                return "redirect:/cursos/gestionar";
            }

            curso.setIdcurso(id);
            if (curso.getActivo() == null) {
                curso.setActivo(true);
            }
            
            cursoService.save(curso);
            redirectAttributes.addFlashAttribute("mensaje", "¡Curso actualizado exitosamente!");
            return "redirect:/cursos/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el curso: " + e.getMessage());
            return "redirect:/cursos/editar/" + id;
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Curso> curso = cursoService.findById(id);
            if (!curso.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El curso no existe");
                return "redirect:/cursos/gestionar";
            }

            cursoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Curso eliminado exitosamente");
            return "redirect:/cursos/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el curso: " + e.getMessage());
            return "redirect:/cursos/gestionar";
        }
    }
} 