package com.joange.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "USUARIO_CURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCurso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USUARIOidusuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "CURSOidcurso")
    private Curso curso;
    
    @Column(name = "fechaasignacion")
    private Date fechaasignacion;
    
    @Column
    private Boolean activo;
} 