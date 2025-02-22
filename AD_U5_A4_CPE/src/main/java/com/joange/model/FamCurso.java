package com.joange.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FAMCURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfamcurso")
    private Long idfamcurso;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBUSOAULAidsubuso", nullable = false)
    private SubUsoAula subUsoAula;
} 