package com.ingweb.springboot.web.app.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DetalleReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetalle")
    private int id;

    @ManyToOne
    @JoinColumn(name = "idautomovil", referencedColumnName = "idautomovil")
    private Automovil automovil;

    @ManyToOne
    @JoinColumn(name = "idreserva", referencedColumnName = "idreserva")
    private Reserva reserva;

    @Column(name = "galgasolina")
    private Date galgasolina;

    @Column( name = "HoraEntrada")
    private LocalDateTime horaEntrada;

    @Column( name = "HoraSalida")
    private LocalDateTime horaSalida;

    @Column( name = "costoxhora")
    private Float costoxhora;

    @Column( name = "estado")
    private boolean estado;
}
