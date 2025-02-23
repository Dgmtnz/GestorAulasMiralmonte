package com.joange.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "RESERVA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreserva")
    private Long idreserva;
    
    @Column(name = "fechadesde")
    @Temporal(TemporalType.DATE)
    private Date fechadesde;
    
    @Column(name = "fechahasta")
    @Temporal(TemporalType.DATE)
    private Date fechahasta;
    
    @Column(name = "horadesde")
    @Temporal(TemporalType.TIME)
    private Date horadesde;
    
    @Column(name = "horahasta")
    @Temporal(TemporalType.TIME)
    private Date horahasta;
    
    @Column(name = "validar")
    private Boolean validar;
    
    @Column(name = "activo")
    private Boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AULAidaula", nullable = false)
    private Aula aula;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIOidusuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURSOidcurso")
    private Curso curso;
} 