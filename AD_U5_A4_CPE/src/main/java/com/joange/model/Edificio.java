package com.joange.model;

import java.util.List;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "EDIFICIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edificio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idedificio")
    private Long idedificio;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column
    private String descripcion;
    
    @Column(name = "npuertasacceso")
    private Integer npuertasacceso;
    
    @Column
    private String ubicacion;
    
    @Column
    private Boolean activo;
    
    @Column
    private String foto;
    
    @Column(name = "nplantas")
    private Integer nplantas = 0;
    
    @OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Planta> plantas;
} 