package com.joange.model;

import javax.persistence.*;

@Entity
@Table(name = "sub_uso_aula")
public class SubUsoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubUso;
    
    @Column
    private String descripcion;
    
    @Column
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "USO_AULA_id_uso")
    private UsoAula usoAula;
    
    // Getters y setters
}
