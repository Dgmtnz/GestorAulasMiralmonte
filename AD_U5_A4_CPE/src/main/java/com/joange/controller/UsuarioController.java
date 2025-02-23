package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.joange.model.Curso;
import com.joange.model.Usuario;
import com.joange.model.UsuarioCurso;
import com.joange.model.UsuarioCursoId;
import com.joange.service.UsuarioService;
import com.joange.service.TipoUsuarioService;
import com.joange.service.CursoService;
import com.joange.util.FileUploadUtil;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @Autowired
    private CursoService cursoService;
    
    @GetMapping("/gestionar")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/listar";
    }
    
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
        return "usuarios/crear";
    }
    
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, 
                                @RequestParam("fotoFile") MultipartFile fotoFile,
                                RedirectAttributes redirectAttributes) {
        try {
            if (usuario.getActivo() == null) {
                usuario.setActivo(true);
            }
            
            // Manejar la foto si se proporciona
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(fotoFile.getOriginalFilename());
                String uploadDir = "src/main/resources/static/images/usuarios/";
                FileUploadUtil.saveFile(uploadDir, fileName, fotoFile);
                usuario.setFoto("/images/usuarios/" + fileName);
            }
            
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            return "redirect:/usuarios/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el usuario: " + e.getMessage());
            return "redirect:/usuarios/crear";
        }
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuario = usuarioService.findById(id);
            if (!usuario.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El usuario no existe");
                return "redirect:/usuarios/gestionar";
            }
            model.addAttribute("usuario", usuario.get());
            return "usuarios/ver";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el usuario: " + e.getMessage());
            return "redirect:/usuarios/gestionar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("cursos", cursoService.findAll());
            model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
            return "usuarios/editar";
        }
        return "redirect:/usuarios/gestionar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, 
                                  @ModelAttribute Usuario usuario,
                                  @RequestParam(required = false) List<Long> cursosIds,
                                  RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.findById(id);
            if (!usuarioExistente.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/usuarios/gestionar";
            }

            Usuario usuarioActual = usuarioExistente.get();
            usuario.setIdusuario(id);
            usuario.setFoto(usuarioActual.getFoto());
            usuario.setContrasenya(usuarioActual.getContrasenya());
            
            // Limpiar los cursos existentes
            usuarioActual.getUsuarioCursos().clear();
            
            // Actualizar cursos solo si se seleccionaron nuevos
            if (cursosIds != null && !cursosIds.isEmpty()) {
                List<UsuarioCurso> usuarioCursos = new ArrayList<>();
                for (Long cursoId : cursosIds) {
                    cursoService.findById(cursoId).ifPresent(curso -> {
                        UsuarioCurso usuarioCurso = new UsuarioCurso();
                        UsuarioCursoId usuarioCursoId = new UsuarioCursoId(usuario.getIdusuario(), cursoId);
                        usuarioCurso.setId(usuarioCursoId);
                        usuarioCurso.setUsuario(usuario);
                        usuarioCurso.setCurso(curso);
                        usuarioCurso.setFechaasignacion(new Date());
                        usuarioCursos.add(usuarioCurso);
                    });
                }
                usuario.setUsuarioCursos(usuarioCursos);
            } else {
                // Si no se seleccionaron cursos, establecer una lista vacía
                usuario.setUsuarioCursos(new ArrayList<>());
            }

            if (usuario.getActivo() == null) {
                usuario.setActivo(true);
            }

            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente");
            return "redirect:/usuarios/gestionar";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            return "redirect:/usuarios/gestionar";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);
            if (!usuarioOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/usuarios/gestionar";
            }

            Usuario usuario = usuarioOpt.get();
            // Verificar si tiene cursos asociados usando usuarioCursos
            if (usuario.getUsuarioCursos() != null && !usuario.getUsuarioCursos().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar el usuario porque tiene cursos asociados. " +
                    "Por favor, elimine primero las asignaciones de cursos.");
                return "redirect:/usuarios/gestionar";
            }

            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            return "redirect:/usuarios/gestionar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
            return "redirect:/usuarios/gestionar";
        }
    }
} 