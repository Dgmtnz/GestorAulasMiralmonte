package com.joange.model;

import javax.persistence.*;

import lombok.Getter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// ... otros imports según las anotaciones que uses

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Long idusuario;
    
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;
    
    @Column(name = "apellido", length = 150, nullable = false)
    private String apellido;
    
    @Column(name = "fechaactivacion")
    private Date fechaactivacion;
    
    @Column(name = "fechadesactivacion")
    private Date fechadesactivacion;
    
    @Column(name = "dni", length = 9, nullable = false, unique = true)
    private String dni;
    
    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;
    
    @Column(name = "contrasenya", length = 8, nullable = false)
    private String contrasenya;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "telefono", length = 9)
    private String telefono;
    
    @Column(name = "foto")
    private String foto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPOUSUARIOidtipousuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioCurso> usuarioCursos = new ArrayList<>();

    // Método helper para mantener la sincronización bidireccional
    public void addUsuarioCurso(UsuarioCurso usuarioCurso) {
        usuarioCursos.add(usuarioCurso);
        usuarioCurso.setUsuario(this);
    }
}
