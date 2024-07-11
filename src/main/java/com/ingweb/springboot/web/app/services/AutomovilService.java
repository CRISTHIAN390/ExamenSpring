package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.entity.Automovil;
import com.ingweb.springboot.web.app.entity.Garaje;
import com.ingweb.springboot.web.app.repositories.AutomovilRepository;
import com.ingweb.springboot.web.app.repositories.GarajeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomovilService {
    @Autowired
    private AutomovilRepository automovilRepository;
    @Autowired
    private GarajeRepository garajeRepository;

    // Método para listar tods los autos
    public List<Automovil> listAll() {
        // Llama al método del repositorio que devuelve tods los clientes activos
        return automovilRepository.findByEstadoTrue();
    }

    // Método para obtener un auto
    public Automovil getById(int id) {
        Optional<Automovil> autoFind = automovilRepository.findById(id);
        return autoFind.get();
    }

    // Método para guardar un auto
    public Automovil save(Automovil auto) throws IOException{
        // Verificar si el garaje existe
        Optional<Garaje> garajeFind = garajeRepository.findById(auto.getGaraje().getId());
        if (garajeFind.isPresent()) {
            // Cambiar el estado del garaje a false
            Garaje garaje = garajeFind.get();
            garaje.setEstado(false);
            garajeRepository.save(garaje);
            auto.setEstado(true);
            return automovilRepository.save(auto);
        } else {
            throw new IllegalArgumentException("Garaje no encontrado");
        }
    }

    // Método para actualizar un auto existente
    public Automovil update(int idauto, Automovil autoactualizado) throws IOException {
        Optional<Automovil> autoOptional = automovilRepository.findById(idauto);
    
        if (autoOptional.isPresent()) {
            Automovil autoExistente = autoOptional.get();
            Garaje garajeAnterior = autoExistente.getGaraje();
            Garaje nuevoGaraje = autoactualizado.getGaraje();
    
            if (garajeAnterior != null && !garajeAnterior.equals(nuevoGaraje)) {
                // Activar el garaje anterior
                garajeAnterior.setEstado(true);
                garajeRepository.save(garajeAnterior);
            }
    
            if (nuevoGaraje != null && !nuevoGaraje.equals(garajeAnterior)) {
                // Desactivar el nuevo garaje
                Optional<Garaje> nuevoGarajeOptional = garajeRepository.findById(nuevoGaraje.getId());
                if (nuevoGarajeOptional.isPresent()) {
                    Garaje garajeToUpdate = nuevoGarajeOptional.get();
                    garajeToUpdate.setEstado(false);
                    garajeRepository.save(garajeToUpdate);
                } else {
                    throw new IllegalArgumentException("Garaje no encontrado con id " + nuevoGaraje.getId());
                }
            }
    
            // Actualizar el automóvil
            autoactualizado.setId(idauto);
            return automovilRepository.save(autoactualizado);
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + idauto);
        }
    }

    // Método para eliminar (desactivar) un auto
    public void delete(int id) throws IOException {
        Optional<Automovil> autoOptional = automovilRepository.findById(id);

        if (autoOptional.isPresent()) {
            Automovil auto = autoOptional.get();
            Garaje garaje = auto.getGaraje();

            // Verificar si el garaje asociado al automóvil existe
            if (garaje != null) {
                Optional<Garaje> garajeOptional = garajeRepository.findById(garaje.getId());
                if (garajeOptional.isPresent()) {
                    Garaje garajeToUpdate = garajeOptional.get();
                    garajeToUpdate.setEstado(true);
                    garajeRepository.save(garajeToUpdate);
                } else {
                    throw new IllegalArgumentException("Garaje no encontrado con id " + garaje.getId());
                }
            }
            auto.setEstado(false);
            automovilRepository.save(auto);
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + id);
        }
    }
}
