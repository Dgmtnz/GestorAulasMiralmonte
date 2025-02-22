package com.joange.model;

import java.util.List;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PLANTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplanta")
    private Long idplanta;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(name = "naulasdocentes", nullable = false)
    private Integer naulasdocentes;
    
    @Column(name = "naulaauxiliares", nullable = false)
    private Integer naulaauxiliares;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDIFICIOidedificio", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "plantas"})
    private Edificio edificio;
    
    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Aula> aulas;
} 