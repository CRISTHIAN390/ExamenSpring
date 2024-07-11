package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.entity.Reserva;
import com.ingweb.springboot.web.app.repositories.ReservaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    //Método para listar tods ls reservs

    public List<Reserva> listxCliente(int idcliente) {
        return   reservaRepository.findByClienteId(idcliente);
    }

    public List<Reserva> listAll() {
        // Llama al método del repositorio que devuelve tods ls reservs
        return reservaRepository.findByEstadoTrue();
    }

    //Método para obtener una reserv
    public Reserva getById(int id) {
        Optional<Reserva> reservaFind = reservaRepository.findById(id);
        return reservaFind.get();
    }

    // Método para guardar  una reserv
    public Reserva save(Reserva reserv) {
        reserv.setEstado(true);
        return reservaRepository.save(reserv);
    }
    
    // Método para actualizar un cliente existente
    public Reserva update(int id, Reserva resource) {
            if (reservaRepository.existsById(id)) {
                resource.setId(id);
                return reservaRepository.save(resource);
            }
            else
                return null;
    }
    
    // Método para eliminar (desactivar) una reserva
    public void delete(int id) {
            Optional<Reserva> reserv = reservaRepository.findById(id);
            reserv.get().setEstado(false);
            reservaRepository.save(reserv.get());
    }


}
