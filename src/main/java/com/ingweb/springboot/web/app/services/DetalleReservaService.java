package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.repositories.AutomovilRepository;
import com.ingweb.springboot.web.app.repositories.DetalleReservaRepository;
import com.ingweb.springboot.web.app.repositories.GarajeRepository;
import com.ingweb.springboot.web.app.repositories.ReservaRepository;

import com.ingweb.springboot.web.app.entity.DetalleReserva;
import com.ingweb.springboot.web.app.entity.Reserva;
import com.ingweb.springboot.web.app.entity.Automovil;
import com.ingweb.springboot.web.app.entity.Garaje;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DetalleReservaService {
    @Autowired
    private DetalleReservaRepository detalleRepository;
    @Autowired
    private ReservaRepository reservRepository;
    @Autowired
    private AutomovilRepository automovilRepository;
    @Autowired
    private GarajeRepository garajeRepository;

    // Método para obtener
    public List<DetalleReserva> list() {
        return detalleRepository.findByEstadoTrue();
    }

    public DetalleReserva getById(int iddetalle) {
        Optional<DetalleReserva> detalleFind = detalleRepository.findById(iddetalle);
        return detalleFind.get();
    }

    // listar detalles de una reserva
    public List<DetalleReserva> listDexResv(int idReserva) {
        return detalleRepository.findByReservaIdAndEstado(idReserva, true);
    }

    // guardar un detalle de una reserva
    @Transactional
    public DetalleReserva save(DetalleReserva detalle) {
        try {
            DetalleReserva det = new DetalleReserva();

            // Buscar la reserva
            Optional<Reserva> reservaa = reservRepository.findById(detalle.getReserva().getId());
            if (reservaa.isPresent()) {
                det.setReserva(reservaa.get());
            } else {
                throw new RuntimeException("No se encontró la reserva");
            }

            // Calcular el costo del detalle actual
            LocalDateTime horaEntrada = detalle.getHoraEntrada();
            LocalDateTime horaSalida = detalle.getHoraSalida();
            float costoxhora = detalle.getCostoxhora();

            Duration duration = Duration.between(horaEntrada, horaSalida);
            long hours = duration.toHours();
            float totalCost = hours * costoxhora;

            // Sumar el costo del detalle actual al costo total de la reserva
            Reserva reserva = det.getReserva();
            float newTotalCost = reserva.getPreciototal() + totalCost;
            reserva.setPreciototal(newTotalCost);
            reservRepository.save(reserva);

            // Establecer los detalles del DetalleReserva
            det.setGalgasolina(detalle.getGalgasolina());
            det.setCostoxhora(costoxhora);
            det.setHoraEntrada(horaEntrada);
            det.setHoraSalida(horaSalida);
            det.setEstado(true);

            // Buscar el automóvil por matrícula
            Optional<Automovil> autoExistente = automovilRepository
                    .findByMatricula(detalle.getAutomovil().getMatricula());
            if (autoExistente.isPresent()) {
                // Actualizar el automóvil existente con el nuevo garaje y estado
                Automovil auto = autoExistente.get();
                auto.setColor(detalle.getAutomovil().getColor());
                auto.setModelo(detalle.getAutomovil().getModelo());
                auto.setMarca(detalle.getAutomovil().getMarca());

                // Desactivar el garaje actual del automóvil
                Garaje garajeActual = auto.getGaraje();
                if (garajeActual != null) {
                    garajeActual.setEstado(true); // Activar el garaje actual
                    garajeRepository.save(garajeActual);
                }

                // Asignar y desactivar el nuevo garaje del automóvil
                Optional<Garaje> nuevoGarajeOptional = garajeRepository
                        .findById(detalle.getAutomovil().getGaraje().getId());
                if (nuevoGarajeOptional.isPresent()) {
                    Garaje nuevoGaraje = nuevoGarajeOptional.get();
                    nuevoGaraje.setEstado(false); // Desactivar el nuevo garaje
                    garajeRepository.save(nuevoGaraje);
                    auto.setGaraje(nuevoGaraje);
                } else {
                    throw new RuntimeException("No se encontró el garaje del nuevo automóvil");
                }

                auto.setEstado(true); // Activar el automóvil
                auto = automovilRepository.save(auto);
                det.setAutomovil(auto); // Asignar el automóvil al DetalleReserva
            } else {
                // Crear un nuevo automóvil
                Automovil auto = new Automovil();
                auto.setMatricula(detalle.getAutomovil().getMatricula());
                auto.setColor(detalle.getAutomovil().getColor());
                auto.setModelo(detalle.getAutomovil().getModelo());
                auto.setMarca(detalle.getAutomovil().getMarca());
                auto.setEstado(true);

                // Asignar y desactivar el garaje del nuevo automóvil
                Optional<Garaje> garaopcional = garajeRepository.findById(detalle.getAutomovil().getGaraje().getId());
                if (garaopcional.isPresent()) {
                    Garaje garaje = garaopcional.get();
                    garaje.setEstado(false); // Desactivar el garaje
                    garajeRepository.save(garaje);
                    auto.setGaraje(garaje);
                } else {
                    throw new RuntimeException("No se encontró el garaje del nuevo automóvil");
                }

                // Guardar el nuevo automóvil
                auto = automovilRepository.save(auto);
                det.setAutomovil(auto); // Asignar el automóvil al DetalleReserva
            }

            // Guardar el DetalleReserva
            return detalleRepository.save(det);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear un detalle", e);
        }
    }

    // Método para actualizar un detalle
    @Transactional
    public DetalleReserva update(int iddetalleanterior, DetalleReserva detalleactualizado) {
        try {
            // Buscar el detalle anterior
            Optional<DetalleReserva> detalleOptional = detalleRepository.findById(iddetalleanterior);
            if (!detalleOptional.isPresent()) {
                throw new RuntimeException("No se encontró el detalle de reserva");
            }

            DetalleReserva detalleAnterior = detalleOptional.get();

            // Buscar la reserva
            Optional<Reserva> reservaa = reservRepository.findById(detalleAnterior.getReserva().getId());
            if (!reservaa.isPresent()) {
                throw new RuntimeException("No se encontró la reserva");
            }

            Reserva reserva = reservaa.get();

            // Calcular el costo del nuevo detalle
            LocalDateTime horaEntrada = detalleactualizado.getHoraEntrada();
            LocalDateTime horaSalida = detalleactualizado.getHoraSalida();
            float costoxhora = detalleactualizado.getCostoxhora();

            Duration duration = Duration.between(horaEntrada, horaSalida);
            long hours = duration.toHours();
            float totalCost = hours * costoxhora;

            // Actualizar el costo total de la reserva
            float previousDetailCost = Duration
                    .between(detalleAnterior.getHoraEntrada(), detalleAnterior.getHoraSalida()).toHours()
                    * detalleAnterior.getCostoxhora();
            float newTotalCost = reserva.getPreciototal() - previousDetailCost + totalCost;
            reserva.setPreciototal(newTotalCost);
            reservRepository.save(reserva);

            // Actualizar los detalles del DetalleReserva
            detalleAnterior.setGalgasolina(detalleactualizado.getGalgasolina());
            detalleAnterior.setCostoxhora(costoxhora);
            detalleAnterior.setHoraEntrada(horaEntrada);
            detalleAnterior.setHoraSalida(horaSalida);
            detalleAnterior.setEstado(true);

            // Guardar el DetalleReserva actualizado
            return detalleRepository.save(detalleAnterior);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar un detalle", e);
        }
    }

    // Eliminar un detalle de mi reserva
    @Transactional
    public void delete(int idDetalleReserva) {
        // Buscar el detalle de reserva por su ID
        Optional<DetalleReserva> detalleOptional = detalleRepository.findById(idDetalleReserva);
        if (detalleOptional.isEmpty()) {
            throw new RuntimeException("No se encontró el detalle de reserva con ID: " + idDetalleReserva);
        }

        DetalleReserva detalleReserva = detalleOptional.get();

        // Desactivar el detalle de reserva (cambio de estado a false)
        detalleReserva.setEstado(false);
        detalleRepository.save(detalleReserva);

        // Obtener el automóvil asociado y desactivarlo
        Automovil automovil = detalleReserva.getAutomovil();
        if (automovil != null) {
            automovil.setEstado(false);
            automovilRepository.save(automovil);

            // Obtener el garaje asociado al automóvil y activarlo
            Garaje garaje = automovil.getGaraje();
            if (garaje != null) {
                garaje.setEstado(true);
                garajeRepository.save(garaje);
            }
        }

        // Calcular el costo del detalle eliminado
        LocalDateTime horaEntrada = detalleReserva.getHoraEntrada();
        LocalDateTime horaSalida = detalleReserva.getHoraSalida();
        float costoPorHora = detalleReserva.getCostoxhora();

        Duration duracion = Duration.between(horaEntrada, horaSalida);
        long horas = duracion.toHours();
        float costoDetalle = horas * costoPorHora;

        // Actualizar el precio total de la reserva
        Reserva reserva = detalleReserva.getReserva();
        float nuevoPrecioTotal = reserva.getPreciototal() - costoDetalle;
        reserva.setPreciototal(nuevoPrecioTotal);
        reservRepository.save(reserva);

    }
}
