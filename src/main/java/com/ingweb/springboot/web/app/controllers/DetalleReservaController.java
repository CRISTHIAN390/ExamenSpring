package com.ingweb.springboot.web.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingweb.springboot.web.app.services.DetalleReservaService;
import com.ingweb.springboot.web.app.entity.*;

import com.ingweb.springboot.web.app.ApiResponse.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RequestMapping("/api/detalles")

public class DetalleReservaController {
    @Autowired
    private DetalleReservaService detalleReservaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DetalleReserva>>> listar() {
        List<DetalleReserva> detalle = detalleReservaService.list();
        ApiResponse<List<DetalleReserva>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de detalles exitosamente");
        response.setData(detalle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<DetalleReserva>>> listDexRes(@PathVariable int id) {
        List<DetalleReserva> detalle = detalleReservaService.listDexResv(id);
        ApiResponse<List<DetalleReserva>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de detalles de un cliente");
        response.setData(detalle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<DetalleReserva>> getDetallexid(@PathVariable int iddetalle) {
        DetalleReserva detalle = detalleReservaService.getById(iddetalle);
        ApiResponse<DetalleReserva> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Obtener un detalle de una reserva");
        response.setData(detalle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DetalleReserva>> crear(@RequestBody DetalleReserva detalleresr) {
        DetalleReserva detalle = detalleReservaService.save(detalleresr);
        ApiResponse<DetalleReserva> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Se registró un detalle exitosamente");
        response.setData(detalle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DetalleReserva>> editar(@PathVariable int id,
            @RequestBody DetalleReserva detalleRequest) {
        DetalleReserva detalle = detalleReservaService.update(id, detalleRequest);
        ApiResponse<DetalleReserva> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("El detalle se actualizó exitosamente");
        response.setData(detalle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DetalleReserva>> delete(@PathVariable int iddetalle) {
        detalleReservaService.delete(iddetalle);
        ApiResponse<DetalleReserva> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Detalle eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
