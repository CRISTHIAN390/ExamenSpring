package com.ingweb.springboot.web.app.services;

import com.ingweb.springboot.web.app.entity.Automovil;
import com.ingweb.springboot.web.app.entity.Garaje;
import com.ingweb.springboot.web.app.repositories.AutomovilRepository;
import com.ingweb.springboot.web.app.repositories.GarajeRepository;

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
    public List<Automovil> list() {
        // Llama al método del repositorio que devuelve tods los autos
        return automovilRepository.findAll();
    }

    // Método para listar tods los autos activos
    public List<Automovil> listactivos() {
        // Llama al método del repositorio que devuelve tods los autos activos
        return automovilRepository.findByEstadoTrue();
    }

    // Método para obtener un auto
    public Automovil getidAuto(int id) {
        Optional<Automovil> autoFind = automovilRepository.findById(id);
        return autoFind.get();
    }

    // Método para guardar un auto
    public Automovil save(Automovil auto) {
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

    public Automovil update(int idauto, Automovil autoactualizado) {
        // Buscar el automóvil por su ID
        Optional<Automovil> autoOptional = automovilRepository.findById(idauto);

        if (autoOptional.isPresent()) {
            Automovil autoExistente = autoOptional.get();

            // Obtener el garaje anterior
            Garaje garajeAnterior = autoExistente.getGaraje();

            // Si el estado del automóvil está cambiando a desactivado
            if (!autoactualizado.isEstado() && autoExistente.isEstado()) {
                // Activar el garaje anterior si existe
                if (garajeAnterior != null) {
                    garajeAnterior.setEstado(true);
                    garajeRepository.save(garajeAnterior);
                }
            } else if (autoactualizado.isEstado() && !autoExistente.isEstado()) {
                // Desactivar el garaje anterior si existe
                if (garajeAnterior != null) {
                    garajeAnterior.setEstado(false);
                    garajeRepository.save(garajeAnterior);
                }
            }

            // Actualizar los campos del automóvil existente con los valores del automóvil
            // actualizado
            autoExistente.setMatricula(autoactualizado.getMatricula());
            autoExistente.setColor(autoactualizado.getColor());
            autoExistente.setModelo(autoactualizado.getModelo());
            autoExistente.setMarca(autoactualizado.getMarca());
            autoExistente.setEstado(autoactualizado.isEstado());

            // Guardar y devolver el automóvil actualizado
            return automovilRepository.save(autoExistente);
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + idauto);
        }
    }

    // Método para eliminar (desactivar) un auto y activar su garaje
    public void delete(int idauto) {
        // Buscar el automóvil por su ID
        Optional<Automovil> autoOptional = automovilRepository.findById(idauto);

        if (autoOptional.isPresent()) {
            Automovil auto = autoOptional.get();
            Garaje garaje = auto.getGaraje();

            // Verificar si el automóvil está activo
            if (auto.isEstado()) {
                // Desactivar el automóvil
                auto.setEstado(false);
                automovilRepository.save(auto);

                // Activar el garaje asociado si existe
                if (garaje != null) {
                    garaje.setEstado(true);
                    garajeRepository.save(garaje);
                }
            } else {
                // Si el automóvil ya está desactivado, no hacer nada
                System.out.println("El automóvil ya está desactivado, no se realizó ninguna acción.");
            }
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + idauto);
        }
    }
}
