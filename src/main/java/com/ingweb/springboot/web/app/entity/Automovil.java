package com.ingweb.springboot.web.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Automovil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idautomovil")
    private int id;

    @OneToOne
    @JoinColumn(name = "idgaraje", referencedColumnName = "idgaraje")
    private Garaje garaje;


    @Column(name = "matricula")
    private String matricula;

    @Column(name = "color")
    private String color;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "marca")
    private String marca;

    @Column(name = "estado")
    private boolean estado;
}
