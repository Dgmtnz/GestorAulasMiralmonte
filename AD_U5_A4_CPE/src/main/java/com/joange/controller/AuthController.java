package com.joange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.joange.model.Usuario;
import com.joange.model.TipoUsuario;
import com.joange.service.UsuarioService;
import com.joange.service.TipoUsuarioService;
import com.joange.util.FileUploadUtil;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.ArrayList;

@Controller
public class AuthController {
    
    private static final String MASTER_PASSWORD = "admin123"; // Contraseña maestra para crear administradores
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false) Boolean registroExitoso) {
        model.addAttribute("usuario", new Usuario());
        if (Boolean.TRUE.equals(registroExitoso)) {
            model.addAttribute("mensajeExito", "¡Registro completado con éxito! Ya puedes iniciar sesión.");
        }
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute Usuario usuario, HttpSession session, Model model) {
        try {
            if (usuarioService.autenticar(usuario.getEmail(), usuario.getContrasenya())) {
                Usuario usuarioAutenticado = usuarioService.findByEmail(usuario.getEmail());
                session.setAttribute("usuarioActual", usuarioAutenticado);
                
                if (usuarioService.esAdministrador(usuarioAutenticado)) {
                    return "redirect:/homeAdmin";
                } else {
                    return "redirect:/home";
                }
            }
            
            model.addAttribute("error", "Credenciales inválidas");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error durante el inicio de sesión: " + e.getMessage());
            return "auth/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null) {
            return "redirect:/login";
        }
        if (usuarioService.esAdministrador(usuario)) {
            return "redirect:/homeAdmin";
        }
        return "home/userHome";
    }
    
    @GetMapping("/homeAdmin")
    public String homeAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null || !usuarioService.esAdministrador(usuario)) {
            return "redirect:/login";
        }
        return "home/adminHome";
    }
    
    @GetMapping("/registro")
    public String showRegistroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }
    
    @PostMapping("/registro")
    public String registrarUsuario(
            @ModelAttribute Usuario usuario,
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
            if (usuarioService.findByEmail(usuario.getEmail()) != null) {
                model.addAttribute("error", "El email ya está registrado");
                return "auth/registro";
            }
            
            if (usuarioService.findByDni(usuario.getDni()) != null) {
                model.addAttribute("error", "El DNI ya está registrado");
                return "auth/registro";
            }
            
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
            
            // Inicializar la lista de cursos vacía
            usuario.setCursos(new ArrayList<>());
            
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