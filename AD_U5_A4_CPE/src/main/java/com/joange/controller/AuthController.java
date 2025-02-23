package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joange.model.Usuario;
import com.joange.model.UsuarioCurso;
import com.joange.model.Curso;
import com.joange.model.TipoUsuario;
import com.joange.service.UsuarioService;
import com.joange.service.TipoUsuarioService;
import com.joange.util.FileUploadUtil;
import com.joange.service.CursoService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {
    
    private static final String MASTER_PASSWORD = "admin123";
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false) Boolean registroExitoso) {
        if (Boolean.TRUE.equals(registroExitoso)) {
            model.addAttribute("mensajeExito", "¡Registro completado con éxito! Ya puedes iniciar sesión.");
        }
        return "auth/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        return "redirect:/login?logout";
    }
    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("cursos", cursoService.findAll());
        return "auth/registro";
    }
    
    @PostMapping("/registro")
    public String registrarUsuario(
            @ModelAttribute Usuario usuario,
            @RequestParam(required = false) List<Long> cursosIds,
            @RequestParam(required = false) MultipartFile fotoFile,
            @RequestParam(required = false) String masterPassword,
            @RequestParam(required = false) Boolean esAdmin,
            Model model) {
        
        try {
            // Validaciones básicas
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                model.addAttribute("error", "El nombre es obligatorio");
                return "auth/registro";
            }
            
            if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                model.addAttribute("error", "El apellido es obligatorio");
                return "auth/registro";
            }
            
            // Verificar email y DNI
            Optional<Usuario> usuarioExistente = usuarioService.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent()) {
                model.addAttribute("error", "El email ya está registrado");
                return "auth/registro";
            }
            
            if (usuarioService.findByDni(usuario.getDni()) != null) {
                model.addAttribute("error", "El DNI ya está registrado");
                return "auth/registro";
            }
            
            // Encriptar contraseña
            usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
            
            // Manejar la foto
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(fotoFile.getOriginalFilename());
                String uploadDir = "src/main/resources/static/images/usuarios/";
                FileUploadUtil.saveFile(uploadDir, fileName, fotoFile);
                usuario.setFoto("/images/usuarios/" + fileName);
            }
            
            // Valores por defecto
            usuario.setFechaactivacion(new Date());
            usuario.setActivo(true);
            
            // Asignar cursos seleccionados
            if (cursosIds != null && !cursosIds.isEmpty()) {
                List<UsuarioCurso> cursos = new ArrayList<>();
                for (Long cursoId : cursosIds) {
                    cursoService.findById(cursoId).ifPresent(curso -> {
                        UsuarioCurso usuarioCurso = new UsuarioCurso();
                        usuarioCurso.setCurso(curso);
                        usuarioCurso.setUsuario(usuario);
                        cursos.add(usuarioCurso);
                    });
                }
                usuario.setUsuarioCursos(cursos);
            } else {
                usuario.setUsuarioCursos(new ArrayList<>());
            }
            
            // Asignar tipo de usuario
            TipoUsuario tipoUsuario;
            if (Boolean.TRUE.equals(esAdmin)) {
                if (!MASTER_PASSWORD.equals(masterPassword)) {
                    model.addAttribute("error", "Master Password incorrecta");
                    return "auth/registro";
                }
                tipoUsuario = tipoUsuarioService.findByNombre("ADMIN");
            } else {
                tipoUsuario = tipoUsuarioService.findByNombre("USER");
            }
            
            if (tipoUsuario == null) {
                model.addAttribute("error", "Error al asignar el tipo de usuario. Tipo de usuario no encontrado.");
                return "auth/registro";
            }
            
            usuario.setTipoUsuario(tipoUsuario);
            
            // Guardar usuario
            Usuario usuarioGuardado = usuarioService.save(usuario);
            if (usuarioGuardado == null) {
                model.addAttribute("error", "Error al guardar el usuario en la base de datos");
                return "auth/registro";
            }
            
            return "redirect:/login?registroExitoso=true";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "auth/registro";
        }
    }
} 