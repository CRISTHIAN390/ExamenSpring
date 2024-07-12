package com.ingweb.springboot.web.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingweb.springboot.web.app.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
      Optional<Cliente> findById(int id);
      
      Optional<Cliente> findByDni(String dni);
      List<Cliente> findByEstado(boolean estado);

      List<Cliente> findByEstadoTrue();
}
