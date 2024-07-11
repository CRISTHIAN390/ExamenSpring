package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.repositories.DetalleReservaRepository;
import com.ingweb.springboot.web.app.repositories.ReservaRepository;
import com.ingweb.springboot.web.app.entity.DetalleReserva;
import com.ingweb.springboot.web.app.entity.Reserva;

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

    // Método para obtener
    public List<DetalleReserva> list() {
        return detalleRepository.findByEstadoTrue();
    }

    public List<DetalleReserva> listDexResv(int idReserva) {
        return detalleRepository.findByReservaId(idReserva);
    }

    // Método para guardar un detalle reserva
    public DetalleReserva save(DetalleReserva detalle) {
        detalle.setEstado(true);
        return detalleRepository.save(detalle);
    }

    // Método para actualizar un detalle existente 
    public DetalleReserva update(int id, DetalleReserva resource) {
        if (detalleRepository.existsById(id)) {
            resource.setId(id);
            return detalleRepository.save(resource);
        } else
            return null;
    }

    public void MontotlResv(int idReserva) {
        List<DetalleReserva> lista = detalleRepository.findByReservaId(idReserva);
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

    // Método para eliminar (desactivar) un detalle
    public void delete(int id) {
        Optional<DetalleReserva> detalle = detalleRepository.findById(id);
        detalle.get().setEstado(false);
        detalleRepository.save(detalle.get());
    }
}
