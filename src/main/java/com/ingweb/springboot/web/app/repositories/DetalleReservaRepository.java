package com.ingweb.springboot.web.app.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingweb.springboot.web.app.entity.DetalleReserva;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva,Integer>{
      Optional<DetalleReserva> findById(int id);
      List<DetalleReserva> findByReservaId(int IdReserva); 
      List<DetalleReserva> findByEstadoTrue();
}
