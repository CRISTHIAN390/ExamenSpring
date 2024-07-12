package com.ingweb.springboot.web.app.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingweb.springboot.web.app.entity.DetalleReserva;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva,Integer>{
      Optional<DetalleReserva> findById(int id);
      List<DetalleReserva> findByReservaId(int idReserva); 
      List<DetalleReserva> findByEstadoTrue();
      List<DetalleReserva> findByReservaIdAndEstado(int idReserva, boolean estado);
}
