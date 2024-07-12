package com.ingweb.springboot.web.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingweb.springboot.web.app.services.ClienteService;
import com.ingweb.springboot.web.app.entity.*;

import com.ingweb.springboot.web.app.ApiResponse.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> listar() {
        List<Cliente> clientes = clienteService.listAll();
        ApiResponse<List<Cliente>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de clients exitosamente");
        response.setData(clientes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crear(@RequestBody Cliente autoRequest) {
        Cliente cliente = clienteService.save(autoRequest);
        ApiResponse<Cliente> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Se registró un client exitosamente");
        response.setData(cliente);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> editar(@PathVariable int id, @RequestBody Cliente autoRequest) {
        Cliente cliente = clienteService.update(id, autoRequest);
        ApiResponse<Cliente> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("El cliente se actualizó exitosamente");
        response.setData(cliente);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> getClientId(@PathVariable int id) {
        Cliente cliente = clienteService.getById(id);
        ApiResponse<Cliente> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Detalle del auto recuperado exitossamente");
        response.setData(cliente);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> delete(@PathVariable int id) {
        clienteService.delete(id);
        ApiResponse<Cliente> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Cliente eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
