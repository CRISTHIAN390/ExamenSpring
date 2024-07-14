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

    public List<Garaje> list() {
        return garajeRepository.findAll();
    }

    public List<Garaje> listGaraActivos() {
        return garajeRepository.findByEstadoTrue();
    }

    public Garaje getById(int id) {
        Optional<Garaje> garajeFind = garajeRepository.findById(id);
        return garajeFind.get();
    }


    public Garaje save(Garaje resource) {
        resource.setEstado(true);
        return garajeRepository.save(resource);
    }

    public Garaje update(int id, Garaje resource) {
        if (garajeRepository.existsById(id)) {
            resource.setId(id);
            return garajeRepository.save(resource);
        } else
            return null;
    }

    public void delete(int id) {
        Optional<Garaje> garaje = garajeRepository.findById(id);
        garaje.get().setEstado(false);
        garajeRepository.save(garaje.get());
    }
}
