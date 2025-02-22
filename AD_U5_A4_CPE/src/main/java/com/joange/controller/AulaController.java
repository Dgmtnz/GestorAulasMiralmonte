package com.joange.controller;

import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.joange.service.AulaService;
import com.joange.service.EdificioService;
import com.joange.service.PlantaService;
import com.joange.service.TipoAulaService;
import com.joange.model.Aula;
import org.springframework.beans.factory.annotation.Value;
import com.joange.util.FileUploadUtil;

@Controller
@RequestMapping("/aulas")
public class AulaController {
    
    @Autowired
    private AulaService aulaService;
    
    @Autowired
    private PlantaService plantaService;
    
    @Autowired
    private TipoAulaService tipoAulaService;
    
    @Autowired
    private FileUploadUtil fileUploadUtil;
    
    @Autowired
    private EdificioService edificioService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/aulas";
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("aula", new Aula());
        model.addAttribute("edificios", edificioService.findActivos());
        model.addAttribute("tiposAula", tipoAulaService.findActivos());
        return "aulas/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarAula(@ModelAttribute Aula aula, 
                            @RequestParam(required = false) MultipartFile fotoFile,
                            RedirectAttributes redirectAttributes) {
        try {
            aula.setIdaula(null); // Aseguramos que es una nueva aula
            
            // Manejo de la foto
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String uniqueFileName = UUID.randomUUID().toString() + "_" + 
                                     fotoFile.getOriginalFilename();
                FileUploadUtil.saveFile(UPLOAD_DIR, uniqueFileName, fotoFile);
                aula.setFoto(uniqueFileName);
            }
            
            // Valores por defecto
            if (aula.getActivo() == null) {
                aula.setActivo(true);
            }
            if (aula.getTieneProyector() == null) {
                aula.setTieneProyector(false);
            }
            if (aula.getTieneAC() == null) {
                aula.setTieneAC(false);
            }
            if (aula.getTieneSmartTV() == null) {
                aula.setTieneSmartTV(false);
            }
            if (aula.getTieneHDMI() == null) {
                aula.setTieneHDMI(false);
            }
            if (aula.getTieneAppleTV() == null) {
                aula.setTieneAppleTV(false);
            }
            
            aulaService.save(aula);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Aula creada exitosamente!");
            return "redirect:/homeAdmin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el aula: " + e.getMessage());
            return "redirect:/aulas/crear";
        }
    }
    
    @GetMapping("/gestionar")
    public String listarAulas(Model model) {
        model.addAttribute("aulas", aulaService.findAll());
        return "aulas/listar";
    }
    
    @GetMapping("/planta/{idplanta}")
    @ResponseBody
    public List<Aula> getAulasPorPlanta(@PathVariable Long idplanta) {
        return aulaService.findByPlantaIdplanta(idplanta);
    }
    
    @GetMapping("/imagen/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 