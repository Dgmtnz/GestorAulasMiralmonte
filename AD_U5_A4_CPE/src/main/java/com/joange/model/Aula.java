package com.joange.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "AULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaula")
    private Long idaula;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Integer capacidad;
    
    @Column(name = "proyector", nullable = false)
    private Boolean tieneProyector;
    
    @Column(name = "aireacondicionado", nullable = false)
    private Boolean tieneAC;
    
    @Column(name = "nenchufes", nullable = false)
    private Integer numOrdenadores;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @Column
    private String foto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANTAidplanta", nullable = false)
    private Planta planta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPOAULAidtipo", nullable = false)
    private TipoAula tipoAula;
    
    @Column(name = "smarttv", nullable = false)
    private Boolean tieneSmartTV;
    
    @Column(name = "hdmi", nullable = false)
    private Boolean tieneHDMI;
    
    @Column(name = "appletv", nullable = false)
    private Boolean tieneAppleTV;
} 