package com.ingweb.springboot.web.app.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingweb.springboot.web.app.services.AutomovilService;
import com.ingweb.springboot.web.app.entity.*;

import com.ingweb.springboot.web.app.ApiResponse.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RequestMapping("/api/autos")
public class AutoController {
    @Autowired
    private  AutomovilService automovilService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Automovil>>> list() {
        List<Automovil> autos = automovilService.list();
        ApiResponse<List<Automovil>> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de autos exitosamente");
        response.setData(autos);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Automovil>> create(@RequestBody Automovil autoRequest){
        Automovil auto= automovilService.save(autoRequest);
        ApiResponse<Automovil> response= new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Se registró un auto exitosamente");
        response.setData(auto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Automovil>> edit(@PathVariable int id, @RequestBody Automovil autoRequest){
        Automovil auto= automovilService.update(id,autoRequest);
        ApiResponse<Automovil> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("El auto se actualizó exitosamente");
        response.setData(auto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Automovil>> getById(@PathVariable int id){
        Automovil automovil= automovilService.getidAuto(id);
        ApiResponse<Automovil> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Detalle del auto recuperado exitossamente");
        response.setData(automovil);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Automovil>> delete(@PathVariable int id){
        automovilService.delete(id);
        ApiResponse<Automovil> response=new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Automovil eliminado exitosamente");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
