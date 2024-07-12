package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.entity.Automovil;
import com.ingweb.springboot.web.app.entity.Cliente;
import com.ingweb.springboot.web.app.entity.DetalleReserva;
import com.ingweb.springboot.web.app.entity.Garaje;
import com.ingweb.springboot.web.app.entity.Reserva;
import com.ingweb.springboot.web.app.repositories.AutomovilRepository;
import com.ingweb.springboot.web.app.repositories.ClienteRepository;
import com.ingweb.springboot.web.app.repositories.DetalleReservaRepository;
import com.ingweb.springboot.web.app.repositories.GarajeRepository;
import com.ingweb.springboot.web.app.repositories.ReservaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AutomovilRepository automovilRepository;
    @Autowired
    private GarajeRepository garajeRepository;
    @Autowired
    private DetalleReservaRepository detalleRepo;
    
    
    //  listar tods ls reservs
    public List<Reserva> HistorialxCliente(int idcliente) {
        return reservaRepository.findByClienteId(idcliente);
    }

    public List<Reserva> listAll() {
        // reservs activas
        return reservaRepository.findByEstadoTrue();
    }

    // Método para obtener una reserv
    public Reserva getById(int id) {
        Optional<Reserva> reservaFind = reservaRepository.findById(id);
        if (reservaFind.isPresent()) {
            return reservaFind.get();
        } else {
            return null;
        }
    }

    // Método para guardar una reserv
    public Reserva save(Reserva reserva) {

        try {
            Reserva reser = new Reserva();
            reser.setFecha(reserva.getFecha());

            reser.setPreciototal((float)0);
            reser.setEstado(true);
            Optional<Cliente> clienteopcional = clienteRepository.findByDni(reserva.getCliente().getDni());
            if (clienteopcional.isPresent()) {
                reser.setCliente(clienteopcional.get());
            } else {
                Cliente clien = reserva.getCliente();
                clien.setEstado(true);
                reser.setCliente(clien);
                clienteRepository.save(clien);
            }
            return reservaRepository.save(reserva);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear", e);
        }
    }

    // Método para actualizar una reserva existente
    public Reserva update(int idrguar, Reserva reservactual) {
        Optional<Reserva> reservafind = reservaRepository.findById(idrguar);
        reservactual.setId(reservafind.get().getId());
        reservafind.get().setFecha(reservactual.getFecha());
        reservafind.get().setPreciototal(reservactual.getPreciototal());

        Cliente clien = reservactual.getCliente();
        Optional<Cliente> clientfind = clienteRepository.findById(clien.getId());
        if (clientfind.isPresent() == false) {
            reservafind.get().setCliente(clien);
            clien.setEstado(true);
            clienteRepository.save(clien);
        }else{
            reservafind.get().setCliente(clientfind.get());
        }
        return reservaRepository.save(reservactual);
    }

    // Método para eliminar (desactivar) una reserva
    @Transactional
    public void delete(int idReserva) {

        List<DetalleReserva> detallesReserva = detalleRepo.findByReservaId(idReserva);

        for (DetalleReserva detalle : detallesReserva) {
            // 1. Desactivar el detalle de reserva
            detalle.setEstado(false);
            detalleRepo.save(detalle);

            // 2. Obtener el auto asociado y desactivarlo
            Automovil auto = detalle.getAutomovil();
            if (auto != null) {
                auto.setEstado(false);
                automovilRepository.save(auto);

                // 3. Obtener el garaje asociado al auto y activarlo
                Garaje garaje = auto.getGaraje();
                if (garaje != null) {
                    garaje.setEstado(true);
                    garajeRepository.save(garaje);
                }
            }
        }    
            // Obtener la reserva por ID
            Optional<Reserva> reserv = reservaRepository.findById(idReserva);
            if (reserv.isPresent()) {
                // Cambiar el estado del cliente asociado a la reserva
                Cliente clie = reserv.get().getCliente();
                if (clie != null) {
                    clie.setEstado(false);
                    clienteRepository.save(clie);
                }
                
                // Cambiar el estado de la reserva
                reserv.get().setEstado(false);
                reservaRepository.save(reserv.get());
            } else {
                throw new IllegalArgumentException("Reserva not found with id: " + idReserva);
            }
        
    }

}
