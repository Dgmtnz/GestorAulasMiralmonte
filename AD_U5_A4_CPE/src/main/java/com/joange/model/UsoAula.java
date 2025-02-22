package com.joange.model;

import javax.persistence.*;

@Entity
@Table(name = "uso_aula")
public class UsoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUso;
    
    @Column
    private String descripcion;
    
    @Column
    private Boolean activo;
    
    // Getters y setters
}
