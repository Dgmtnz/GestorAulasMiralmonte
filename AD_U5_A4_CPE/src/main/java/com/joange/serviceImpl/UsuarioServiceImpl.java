package com.joange.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
// ... otros imports según las anotaciones que uses

@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoUsuarioService tipoUsuarioService;
    
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public Optional<Usuario> findByIdusuario(Long idusuario) {
        return usuarioRepository.findById(idusuario);
    }
    
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    public Usuario findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }
    
    @Override
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
    public void deleteByIdusuario(Long idusuario) {
        usuarioRepository.deleteById(idusuario);
    }
    
    @Override
    public List<Usuario> findActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    
    @Override
    public boolean autenticar(String email, String contrasenya) {
        Usuario usuario = findByEmail(email);
        return usuario != null && 
               usuario.getActivo() && 
               usuario.getContrasenya().equals(contrasenya);
    }
    
    @Override
    public boolean esAdministrador(Usuario usuario) {
        return usuario != null && 
               usuario.getTipoUsuario() != null && 
               "ADMIN".equals(usuario.getTipoUsuario().getNombre());
    }
} 