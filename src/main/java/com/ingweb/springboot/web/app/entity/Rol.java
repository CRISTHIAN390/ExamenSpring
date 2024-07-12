package com.ingweb.springboot.web.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rol") // Especifica el nombre de la tabla correctamente
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "estado")
    private boolean estado;
}
