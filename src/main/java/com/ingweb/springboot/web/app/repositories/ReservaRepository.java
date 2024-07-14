package com.ingweb.springboot.web.app.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingweb.springboot.web.app.entity.Reserva;
import java.util.List;
import java.util.Optional;
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    Optional<Reserva> findById(int id);
    List<Reserva> findByClienteDni(String idcliente);
    List<Reserva> findByEstadoTrue();
}