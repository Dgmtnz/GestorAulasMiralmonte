package com.joange.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCursoId implements Serializable {
    
    @Column(name = "USUARIOidusuario")
    private Long usuarioId;
    
    @Column(name = "CURSOidcurso")
    private Long cursoId;
} 