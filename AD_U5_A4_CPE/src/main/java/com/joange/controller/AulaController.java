package com.joange.controller;

import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
import org.springframework.util.StringUtils;

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
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/aulas/";
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("aula", new Aula());
        model.addAttribute("plantas", plantaService.findActivos());
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "aulas/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarAula(@ModelAttribute Aula aula, 
                         @RequestParam("fotoFile") MultipartFile fotoFile,
                         RedirectAttributes redirectAttributes) {
        try {
            aula.setIdaula(null);
            if (aula.getActivo() == null) {
                aula.setActivo(true);
            }

            if (!fotoFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(fotoFile.getOriginalFilename());
                FileUploadUtil.saveFile(UPLOAD_DIR, fileName, fotoFile);
                aula.setFoto("/images/aulas/" + fileName);
            }

            aulaService.save(aula);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Aula creada exitosamente!");
            return "redirect:/aulas/gestionar";
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

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Aula aula = aulaService.findByIdaula(id);
        if (aula == null) {
            return "redirect:/aulas/gestionar";
        }
        model.addAttribute("aula", aula);
        model.addAttribute("plantas", plantaService.findActivos());
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "aulas/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarAula(@PathVariable Long id, 
                            @ModelAttribute Aula aula,
                            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
                            RedirectAttributes redirectAttributes) {
        try {
            Aula aulaExistente = aulaService.findByIdaula(id);
            if (aulaExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Aula no encontrada");
                return "redirect:/aulas/gestionar";
            }
            
            aula.setIdaula(id);
            if (aula.getActivo() == null) {
                aula.setActivo(true);
            }
            
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(fotoFile.getOriginalFilename());
                FileUploadUtil.saveFile(UPLOAD_DIR, fileName, fotoFile);
                aula.setFoto("/images/aulas/" + fileName);
            } else {
                aula.setFoto(aulaExistente.getFoto());
            }
            
            aulaService.save(aula);
            redirectAttributes.addFlashAttribute("mensajeExito", "Aula actualizada exitosamente");
            return "redirect:/aulas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el aula: " + e.getMessage());
            return "redirect:/aulas/editar/" + id;
        }
    }

    @GetMapping("/ver/{id}")
    public String verAula(@PathVariable Long id, Model model) {
        Aula aula = aulaService.findByIdaula(id);
        if (aula == null) {
            return "redirect:/aulas/gestionar";
        }
        model.addAttribute("aula", aula);
        return "aulas/ver";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Aula aula = aulaService.findByIdaula(id);
            if (aula == null) {
                redirectAttributes.addFlashAttribute("error", "Aula no encontrada");
                return "redirect:/aulas/gestionar";
            }

            aulaService.deleteByIdaula(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Aula eliminada exitosamente");
            return "redirect:/aulas/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el aula: " + e.getMessage());
            return "redirect:/aulas/gestionar";
        }
    }

    @GetMapping("/planta/{idplanta}")
    @ResponseBody
    public List<Map<String, Object>> getAulas(@PathVariable Long idplanta) {
        List<Aula> aulas = aulaService.findActivosByPlanta(idplanta);
        return aulas.stream()
            .map(a -> {
                Map<String, Object> aulaMap = new HashMap<>();
                aulaMap.put("idaula", a.getIdaula());
                aulaMap.put("nombre", a.getNombre());
                return aulaMap;
            })
            .collect(Collectors.toList());
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