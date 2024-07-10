package com.ingweb.springboot.web.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private int id;

    @Column(name = "nombre")
    private int nombre;

    @Column(name = "estado")
    private boolean estado;
}
