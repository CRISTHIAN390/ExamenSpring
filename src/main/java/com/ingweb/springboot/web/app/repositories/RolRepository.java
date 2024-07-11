package com.ingweb.springboot.web.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ingweb.springboot.web.app.entity.Rol;
import java.util.Optional;
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findById(int id);
    Optional<Rol> findByNombre(String nombre);
}
