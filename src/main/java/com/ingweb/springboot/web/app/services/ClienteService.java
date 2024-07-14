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


    public List<Cliente> listAll() {
        return clienteRepository.findAll();
    }

    
    public Cliente getById(int id) {
        Optional<Cliente> clienteFind = clienteRepository.findById(id);
        return clienteFind.get();
    }

    public Cliente save(Cliente resource) {
        Optional<Cliente> clienteFind = clienteRepository.findByDni(resource.getDni());
        if (clienteFind.isPresent()) {
            resource.setId(clienteFind.get().getId());
            return clienteRepository.save(resource);
        }
        clienteFind.get().setNombres(resource.getNombres());
        clienteFind.get().setApellidos(resource.getApellidos());
        clienteFind.get().setDni(resource.getDni());
        clienteFind.get().setDireccion(resource.getDireccion());
        clienteFind.get().setTelefono(resource.getTelefono());
        clienteFind.get().setEstado(true);
        return clienteRepository.save(clienteFind.get());
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
        cliente.get().setId(id);
        cliente.get().setEstado(false);
        clienteRepository.save(cliente.get());
    }
}
