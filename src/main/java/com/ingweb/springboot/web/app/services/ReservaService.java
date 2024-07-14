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

    public List<Reserva> HistorialxCliente(String dni) {
        return reservaRepository.findByClienteDni(dni);
    }

    public List<Reserva> listAll() {
        return reservaRepository.findByEstadoTrue();
    }

    public Reserva getById(int id) {
        Optional<Reserva> reservaFind = reservaRepository.findById(id);
        if (reservaFind.isPresent()) {
            return reservaFind.get();
        } else {
            return null;
        }
    }

    public Reserva save(Reserva reserva) {
        try {
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setFecha(reserva.getFecha());
            nuevaReserva.setPreciototal(0f);
            nuevaReserva.setEstado(true);

            Cliente cliente;
            Optional<Cliente> clienteOpcional = clienteRepository.findByDni(reserva.getCliente().getDni());
            if (clienteOpcional.isPresent()) {
                cliente = clienteOpcional.get();
            } else {
                cliente = reserva.getCliente();
                cliente.setEstado(true);
                cliente = clienteRepository.save(cliente); // Guardar y obtener el cliente con ID
            }

            nuevaReserva.setCliente(cliente); // Asignar cliente con ID

            return reservaRepository.save(nuevaReserva);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la reserva", e);
        }
    }

    // Método para actualizar una reserva existente
    public Reserva update(int idrguar, Reserva reservactual) {
        try {
            Optional<Reserva> reservafind = reservaRepository.findById(idrguar);
            if (!reservafind.isPresent()) {
                throw new RuntimeException("Reserva no encontrada");
            }
            Reserva reservaExistente = reservafind.get();

            // Actualizar los detalles de la reserva
            reservaExistente.setFecha(reservactual.getFecha());
            reservaExistente.setPreciototal(reservactual.getPreciototal());

            // Obtener el cliente actual de la reserva existente
            Cliente clienteExistente = reservaExistente.getCliente();

            // Obtener el cliente de la reserva actualizada
            Cliente clienteActual = reservactual.getCliente();

            // Comprobar si el cliente es el mismo o si se cambió a otro
            if (clienteExistente.getId() != clienteActual.getId()) {
                // Si el cliente es diferente, asignar el nuevo cliente a la reserva
                Optional<Cliente> clientfind = clienteRepository.findById(clienteActual.getId());
                if (!clientfind.isPresent()) {
                    // Si el cliente no existe, lanzar una excepción
                    throw new RuntimeException("Cliente no encontrado");
                } else {
                    reservaExistente.setCliente(clientfind.get());
                }
            }
            // Guardar los cambios en la reserva
            return reservaRepository.save(reservaExistente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la reserva", e);
        }
    }

    // Método para eliminar (desactivar) una reserva
    @Transactional
    public void delete(int idReserva) {
        try {
            // 1. Obtener los detalles de la reserva y desactivarlos
            List<DetalleReserva> detallesReserva = detalleRepo.findByReservaId(idReserva);
            for (DetalleReserva detalle : detallesReserva) {
                detalle.setEstado(false);
                detalleRepo.save(detalle);

                // 2. Obtener el auto asociado que tenga estado true y desactivarlo
                Automovil auto = detalle.getAutomovil();
                if (auto != null && auto.isEstado()) { // Verificar que el estado del auto sea true
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

            // 4. Cambiar el estado de la reserva
            Optional<Reserva> reserv = reservaRepository.findById(idReserva);
            if (reserv.isPresent()) {
                reserv.get().setEstado(false);
                reservaRepository.save(reserv.get());
            } else {
                throw new IllegalArgumentException("La reserva no existe: " + idReserva);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva", e);
        }
    }

}
