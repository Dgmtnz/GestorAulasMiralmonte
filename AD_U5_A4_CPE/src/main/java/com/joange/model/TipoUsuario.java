package com.joange.model;

import javax.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "TIPOUSUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TipoUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtipousuario;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column
    private String descripcion;
    
    @Column
    private Boolean activo;
    
    @OneToMany(mappedBy = "tipoUsuario")
    private List<Usuario> usuarios;
}