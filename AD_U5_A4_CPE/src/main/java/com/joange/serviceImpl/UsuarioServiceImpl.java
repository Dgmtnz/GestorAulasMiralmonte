package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.joange.model.Usuario;
import com.joange.repository.UsuarioRepository;
import com.joange.service.TipoUsuarioService;
import com.joange.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.joange.model.TipoUsuario;
import com.joange.model.Curso;
// ... otros imports según las anotaciones que uses

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuario.getIdusuario() == null) {
            usuario.setFechaactivacion(new Date());
            if (usuario.getActivo() == null) {
                usuario.setActivo(true);
            }
        }
        return usuarioRepository.save(usuario);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }
    
    @Override
    public Usuario findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }
    
    @Override
    public boolean autenticar(String email, String contrasenya) {
        Optional<Usuario> usuarioOpt = findByEmail(email);
        return usuarioOpt.isPresent() && 
               usuarioOpt.get().getActivo() && 
               usuarioOpt.get().getContrasenya().equals(contrasenya);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean esAdministrador(Usuario usuario) {
        if (usuario == null || usuario.getTipoUsuario() == null) {
            return false;
        }
        
        // Recargar el usuario para asegurar que el tipo de usuario esté cargado
        Usuario usuarioRecargado = findById(usuario.getIdusuario()).orElse(null);
        if (usuarioRecargado == null) {
            return false;
        }
        
        return "ADMIN".equalsIgnoreCase(usuarioRecargado.getTipoUsuario().getNombre());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Usuario findByIdWithCursos(Long id) {
        return usuarioRepository.findByIdWithCursos(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Curso> findCursosByUsuarioId(Long usuarioId) {
        return usuarioRepository.findCursosByUsuarioId(usuarioId);
    }
} 