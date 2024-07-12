package com.ingweb.springboot.web.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcliente")
    private int id;
    
    // Muchos clientes pueden ser registrados por un usuario
    @ManyToOne
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    private User user;


    @Column(name = "dni")
    private String dni;

    @Column(name = "apellidos")
    private String apellidos;
    
    @Column(name = "nombres")
    private String nombres;

    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "telefono")
    private String telefono;

    @Column(name = "estado")
    private boolean estado;
}
