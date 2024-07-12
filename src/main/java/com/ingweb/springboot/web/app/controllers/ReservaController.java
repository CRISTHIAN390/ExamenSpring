package com.ingweb.springboot.web.app.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingweb.springboot.web.app.services.ReservaService;
import com.ingweb.springboot.web.app.entity.*;

import com.ingweb.springboot.web.app.ApiResponse.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private  ReservaService reservaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Reserva>>> listresv() {
        List<Reserva> reservs = reservaService.listAll();
        ApiResponse<List<Reserva>> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de reservas exitosamente");
        response.setData(reservs);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ApiResponse<List<Reserva>>> Hiscliente(@PathVariable int id) {
        List<Reserva> reservs = reservaService.HistorialxCliente(id);
        ApiResponse<List<Reserva>> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de reservas de un cliente");
        response.setData(reservs);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Reserva>> create(@RequestBody Reserva reservaRequest){
        Reserva reserva= reservaService.save(reservaRequest);
        ApiResponse<Reserva> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Se registró un auto exitosamente");
        response.setData(reserva);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Reserva>> edit(@PathVariable int id, @RequestBody Reserva reservaRequest){
        Reserva reserva= reservaService.update(id,reservaRequest);
        ApiResponse<Reserva> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("El reserva se actualizó exitosamente");
        response.setData(reserva);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reserva>> getById(@PathVariable int id){
        Reserva reserva= reservaService.getById(id);
        ApiResponse<Reserva> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Detalle de la reserva recuperado exitossamente");
        response.setData(reserva);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Reserva>> delete(@PathVariable int id){
        reservaService.delete(id);
        ApiResponse<Reserva> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Reserva eliminado exitosamente");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
