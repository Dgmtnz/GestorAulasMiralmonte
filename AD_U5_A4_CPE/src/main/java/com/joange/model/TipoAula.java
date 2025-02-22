package com.joange.model;

import java.util.List;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "TIPOAULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoAula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo")
    private Long idtipo;
    
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @OneToMany(mappedBy = "tipoAula", fetch = FetchType.LAZY)
    private List<Aula> aulas;
} 