package com.ingweb.springboot.web.app.entity;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user") // Especifica el nombre de la tabla correctamente
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private int id;

    @ManyToOne
    @JoinColumn(name = "idrol", referencedColumnName = "idrol")
    private Rol rol;

    @Column(nullable = false, name = "username")
    String username;
    @Column(nullable = false, name = "email")
    String email;
    @Column(nullable = false, name = "password")
    String password;
    @Column(name = "estado")
    boolean estado;
}
