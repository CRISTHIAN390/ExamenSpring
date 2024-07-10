package com.ingweb.springboot.web.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Garaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idgaraje")
    private int id;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "estado")
    private boolean estado;
}