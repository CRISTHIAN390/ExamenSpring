package com.ingweb.springboot.web.app.entity;
 
import java.sql.Date;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreserva")
    private int id;

    // Muchas reservas pueden ser registradas por un cliente
    @ManyToOne
    @JoinColumn(name = "idcliente", referencedColumnName = "idcliente")
    private Cliente cliente;

    @Column( name = "preciototal")
    private Float preciototal;
    @Column(name = "fecha")
    private Date fechareserva;
    @Column( name = "galgasolina")
    private Float galgasolina;
    @Column( name = "estado")
    private boolean estado;
}
