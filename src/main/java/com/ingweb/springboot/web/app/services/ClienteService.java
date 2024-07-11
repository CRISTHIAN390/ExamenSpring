package com.ingweb.springboot.web.app.services;


import com.ingweb.springboot.web.app.repositories.ClienteRepository;
import com.ingweb.springboot.web.app.entity.Cliente;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    // Método para listar tods los clientes
    public List<Cliente> listAll() {
        // Llama al método del repositorio que devuelve tods los clientes activos
        return clienteRepository.findByEstadoTrue();
    }

    // Método para obtener un cliente
    public Cliente getById(int id) {
        Optional<Cliente> clienteFind = clienteRepository.findById(id);
        return clienteFind.get();
    }

    // Método para guardar un cliente
    public Cliente save(Cliente resource) {
        resource.setEstado(true);
        return clienteRepository.save(resource);
    }
    // Método para actualizar un cliente existente
    public Cliente update(int id, Cliente resource) {
        if (clienteRepository.existsById(id)) {
            resource.setId(id);
            return clienteRepository.save(resource);
        } else
            return null;
    }

    // Método para eliminar (desactivar) un cliente
    public void delete(int id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        cliente.get().setEstado(false);
        clienteRepository.save(cliente.get());
    }
}
