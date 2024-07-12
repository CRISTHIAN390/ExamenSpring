package com.ingweb.springboot.web.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ingweb.springboot.web.app.entity.Automovil;

public interface AutomovilRepository extends JpaRepository<Automovil, Integer> {
    Optional<Automovil> findById(int id);
    Optional<Automovil> findByMatricula(String matricula);
    List<Automovil> findByEstado(boolean estado);

    List<Automovil> findByEstadoTrue();
}
