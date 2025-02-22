package com.joange.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private Long idcurso;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAMCURSOidfamcurso", nullable = false)
    private FamCurso famCurso;

    @OneToMany(mappedBy = "curso")
    private List<UsuarioCurso> usuarios = new ArrayList<>();
} 