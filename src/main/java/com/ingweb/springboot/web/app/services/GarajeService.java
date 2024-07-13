package com.ingweb.springboot.web.app.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ingweb.springboot.web.app.entity.Garaje;
import com.ingweb.springboot.web.app.repositories.GarajeRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GarajeService {
    // Inyección de dependencias de GarajeRepository
    @Autowired
    private GarajeRepository garajeRepository;

    // Método para listar tods los garajes
    public List<Garaje> list() {
        // Llama al método del repositorio que devuelve tods los garajes 
        return garajeRepository.findAll();
    }

    // Método para listar tods los garajes
    public List<Garaje> listActivos() {
        // Llama al método del repositorio que devuelve tods los garajes activos 
        return garajeRepository.findByEstadoTrue();
    }



    // Método para obtener un garaje
    public Garaje getById(int id) {
        Optional<Garaje> garajeFind = garajeRepository.findById(id);
        return garajeFind.get();
    }

    // Método para guardar un garaje
    public Garaje save(Garaje resource) {
        resource.setEstado(true);
        return garajeRepository.save(resource);
    }

    // Método para actualizar un garaje existente
    public Garaje update(int id, Garaje resource) {
        if (garajeRepository.existsById(id)) {
            resource.setId(id);
            return garajeRepository.save(resource);
        } else
            return null;
    }

    // Método para eliminar (desactivar) un garaje
    public void delete(int id) {
        Optional<Garaje> garaje = garajeRepository.findById(id);
        garaje.get().setEstado(false);
        garajeRepository.save(garaje.get());
    }
}
