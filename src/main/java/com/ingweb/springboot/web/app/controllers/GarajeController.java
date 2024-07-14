package com.ingweb.springboot.web.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingweb.springboot.web.app.entity.Garaje;
import com.ingweb.springboot.web.app.services.GarajeService;

import com.ingweb.springboot.web.app.ApiResponse.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RequestMapping("/api/garajes")
public class GarajeController {
    @Autowired
    private  GarajeService garajeservice;
    @GetMapping
    public ResponseEntity<ApiResponse<List<Garaje>>> list() {
        List<Garaje> garajes = garajeservice.list();
        ApiResponse<List<Garaje>> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de Garajes exitosamente");
        response.setData(garajes);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<Garaje>>> listactivos() {
        List<Garaje> garajes = garajeservice.listGaraActivos();
        ApiResponse<List<Garaje>> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de Garajes activos exitosamente");
        response.setData(garajes);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Garaje>> create(@RequestBody Garaje garajeRequest){
        Garaje garaje= garajeservice.save(garajeRequest);
        ApiResponse<Garaje> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Se registró un garaje exitosamente");
        response.setData(garaje);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Garaje>> edit(@PathVariable int id, @RequestBody Garaje garajeRequest){
        Garaje garaje= garajeservice.update(id,garajeRequest);
        ApiResponse<Garaje> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("La garaje se actualizó exitosamente");
        response.setData(garaje);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Garaje>> getById(@PathVariable int id){
        Garaje garaje= garajeservice.getById(id);
        ApiResponse<Garaje> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Detalle del garaje recuperado exitossamente");
        response.setData(garaje);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Garaje>> delete(@PathVariable int id){
        garajeservice.delete(id);
        ApiResponse<Garaje> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Garaje eliminado exitosamente");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}