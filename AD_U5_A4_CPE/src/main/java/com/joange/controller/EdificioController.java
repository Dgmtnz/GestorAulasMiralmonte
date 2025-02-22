package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.joange.service.EdificioService;
import com.joange.model.Edificio;
import com.joange.util.FileUploadUtil;
import java.util.UUID;
import java.io.File;


@Controller
@RequestMapping("/edificios")
public class EdificioController {
    private static final String UPLOAD_DIR = "src/main/resources/static/images/edificios/";
    
    @Autowired
    private EdificioService edificioService;
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        if (!model.containsAttribute("edificio")) {
            model.addAttribute("edificio", new Edificio());
        }
        return "edificios/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarEdificio(@ModelAttribute Edificio edificio, 
                                 @RequestParam(required = false) MultipartFile fotoFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            edificio.setIdedificio(null);
            
            // Manejo de la foto
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String uniqueFileName = UUID.randomUUID().toString() + "_" + 
                                     fotoFile.getOriginalFilename();
                FileUploadUtil.saveFile(UPLOAD_DIR, uniqueFileName, fotoFile);
                edificio.setFoto(uniqueFileName);
            }
            
            // Valores por defecto
            if (edificio.getActivo() == null) {
                edificio.setActivo(true);
            }
            if (edificio.getNplantas() == null) {
                edificio.setNplantas(0);
            }
            
            edificioService.save(edificio);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Edificio creado exitosamente!");
            return "redirect:/homeAdmin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el edificio: " + e.getMessage());
            return "redirect:/edificios/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarEdificios(Model model) {
        model.addAttribute("edificios", edificioService.findAll());
        return "edificios/listar";
    }

    @PostMapping("/save")
    public String saveEdificio(@ModelAttribute Edificio edificio, RedirectAttributes redirectAttributes) {
        edificioService.save(edificio);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Edificio creado correctamente!");
        return "redirect:/homeAdmin";
    }
} 