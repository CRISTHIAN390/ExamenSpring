package com.ingweb.springboot.web.app.repositories;

import com.ingweb.springboot.web.app.entity.Garaje;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarajeRepository extends JpaRepository<Garaje, Integer> {

    Optional<Garaje> findById(int id);

    List<Garaje> findByEstadoTrue();
}
