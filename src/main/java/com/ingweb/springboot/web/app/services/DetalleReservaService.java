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

    public List<DetalleReserva> listDexResv(int idReserva) {
        return detalleRepository.findByReservaIdAndEstado(idReserva, true);
    }

    // Motonto total
    public void MontotlResv(int idReserva) {
        List<DetalleReserva> lista = detalleRepository.findByReservaIdAndEstado(idReserva, true);
        // Verificar si la reserva existe
        Optional<Reserva> reservaOptional = reservRepository.findById(idReserva);
        if (!reservaOptional.isPresent()) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }

        Reserva reserva = reservaOptional.get();
        float montoTotal = 0;

        for (DetalleReserva detalle : lista) {
            LocalDateTime horaEntrada = detalle.getHoraEntrada();
            LocalDateTime horaSalida = detalle.getHoraSalida();

            // Verificar si horaEntrada y horaSalida no son nulos
            if (horaEntrada != null && horaSalida != null) {
                int diferenciaHoras = (int) Duration.between(horaEntrada, horaSalida).toHours();
                montoTotal += detalle.getCostoxhora() * diferenciaHoras;
            }
        }
        reserva.setPreciototal(montoTotal);
        reservRepository.save(reserva);
    }

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

            // Buscar y actualizar el automovil y garaje
            Automovil auto;
            Optional<Automovil> autoopcional = automovilRepository.findById(detalle.getAutomovil().getId());
            if (autoopcional.isPresent()) {
                auto = autoopcional.get();
                auto.setEstado(true);
            } else {
                // Crear un nuevo automóvil si no existe
                auto = new Automovil();
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
            }
            automovilRepository.save(auto);
            det.setAutomovil(auto);

            return detalleRepository.save(det);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear un detalle", e);
        }
    }

    // Método para actualizar un detalle
    public DetalleReserva update(int iddetalleanterior, DetalleReserva detalleactualizado) {
        try {
            // Buscar el detalle anterior
            Optional<DetalleReserva> detalleOptional = detalleRepository.findById(iddetalleanterior);
            if (!detalleOptional.isPresent()) {
                throw new RuntimeException("No se encontró el detalle de reserva");
            }

            DetalleReserva detalleAnterior = detalleOptional.get();
            DetalleReserva detalle = detalleactualizado;

            // Buscar la reserva
            Optional<Reserva> reservaa = reservRepository.findById(detalle.getReserva().getId());
            if (reservaa.isPresent()) {
                detalle.setReserva(reservaa.get());
            } else {
                throw new RuntimeException("No se encontró la reserva");
            }

            // Calcular el costo del nuevo detalle
            LocalDateTime horaEntrada = detalle.getHoraEntrada();
            LocalDateTime horaSalida = detalle.getHoraSalida();
            float costoxhora = detalle.getCostoxhora();

            Duration duration = Duration.between(horaEntrada, horaSalida);
            long hours = duration.toHours();
            float totalCost = hours * costoxhora;

            // Sumar el costo del detalle actual al costo total de la reserva
            Reserva reserva = detalle.getReserva();
            float previousTotalCost = reserva.getPreciototal();
            float previousDetailCost = Duration
                    .between(detalleAnterior.getHoraEntrada(), detalleAnterior.getHoraSalida()).toHours()
                    * detalleAnterior.getCostoxhora();
            float newTotalCost = previousTotalCost - previousDetailCost + totalCost;
            reserva.setPreciototal(newTotalCost);
            reservRepository.save(reserva);

            // Actualizar los detalles del DetalleReserva
            detalle.setId(iddetalleanterior);
            detalle.setGalgasolina(detalle.getGalgasolina());
            detalle.setCostoxhora(costoxhora);
            detalle.setHoraEntrada(horaEntrada);
            detalle.setHoraSalida(horaSalida);
            detalle.setEstado(true);

            // Actualizar el automóvil y verificar cambios de estado
            Automovil autoAnterior = detalleAnterior.getAutomovil();
            Automovil autoActualizado = detalle.getAutomovil();
            if (autoAnterior.getId() != autoActualizado.getId()) {
                autoAnterior.setEstado(false);
                automovilRepository.save(autoAnterior);
                autoActualizado.setEstado(true);
                automovilRepository.save(autoActualizado);

                // Verificar cambios en el garaje
                if (autoAnterior.getGaraje().getId() != autoActualizado.getGaraje().getId()) {
                    Garaje garajeAnterior = autoAnterior.getGaraje();
                    Garaje garajeActualizado = autoActualizado.getGaraje();
                    garajeAnterior.setEstado(true);
                    garajeRepository.save(garajeAnterior);
                    garajeActualizado.setEstado(false);
                    garajeRepository.save(garajeActualizado);
                }
            } else {
                // Verificar solo cambios en el garaje
                if (autoAnterior.getGaraje().getId() != autoActualizado.getGaraje().getId()) {
                    Garaje garajeAnterior = autoAnterior.getGaraje();
                    Garaje garajeActualizado = autoActualizado.getGaraje();
                    garajeAnterior.setEstado(true);
                    garajeRepository.save(garajeAnterior);
                    garajeActualizado.setEstado(false);
                    garajeRepository.save(garajeActualizado);
                }
            }

            detalle.setAutomovil(autoActualizado);

            return detalleRepository.save(detalle);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar un detalle", e);
        }
    }

    public void delete(int iddetallereserva) {

        Optional<DetalleReserva> detalle = detalleRepository.findById(iddetallereserva);
        if (!detalle.isPresent()) {
            throw new RuntimeException("No se encontró el detalle de reserva");
        }



        if (detalle != null) {
            // 1. Desactivar el detalle de reserva
            detalle.get().setEstado(false);
            detalleRepository.save( detalle.get());

            // 2. Obtener el auto asociado y desactivarlo
            Automovil auto = detalle.get().getAutomovil();
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
    }
}
