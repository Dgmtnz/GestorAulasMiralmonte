package com.joange.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "USUARIO_CURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCurso {
    
    @EmbeddedId
    private UsuarioCursoId id;
    
    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "USUARIOidusuario")
    private Usuario usuario;
    
    @ManyToOne
    @MapsId("cursoId")
    @JoinColumn(name = "CURSOidcurso")
    private Curso curso;
    
    @Column(name = "fechaasignacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaasignacion;
} 