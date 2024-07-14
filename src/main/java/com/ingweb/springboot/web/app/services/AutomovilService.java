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
        return automovilRepository.findAll();
    }

    // Método para listar tods los autos activos
    public List<Automovil> listactivos() {
        return automovilRepository.findByEstadoTrue();
    }

    // Método para obtener un auto
    public Automovil getidAuto(String matricula) {
        Optional<Automovil> autoFind = automovilRepository.findByMatricula(matricula);
        return autoFind.get();
    }

    // Método para guardar un auto
    public Automovil save(Automovil auto) {

        Optional<Garaje> garajeFind = garajeRepository.findById(auto.getGaraje().getId());
        if (garajeFind.isPresent()) {
            Garaje garaje = garajeFind.get();
            garaje.setEstado(false);
            garajeRepository.save(garaje);
            auto.setEstado(true);
            return automovilRepository.save(auto);
        } else {
            throw new IllegalArgumentException("Garaje no encontrado");
        }
    }

    public Automovil update(String idauto, Automovil autoactualizado) {
        Optional<Automovil> autoOptional = automovilRepository.findByMatricula(idauto);
        if (autoOptional.isPresent()) {
            Automovil autoExistente = autoOptional.get();

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
            // Actualizamos los campos del automóvil
            autoExistente.setMatricula(autoactualizado.getMatricula());
            autoExistente.setColor(autoactualizado.getColor());
            autoExistente.setModelo(autoactualizado.getModelo());
            autoExistente.setMarca(autoactualizado.getMarca());
            autoExistente.setEstado(autoactualizado.isEstado());

            return automovilRepository.save(autoExistente);
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + idauto);
        }
    }

    // Método para  (desactivar) un auto y activar su garaje
    public void delete(String matriculauto) {
        // Buscar el automóvil por su ID
        Optional<Automovil> autoOptional = automovilRepository.findByMatricula(matriculauto);

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
                System.out.println("El automóvil ya está desactivado existosamente");
            }
        } else {
            throw new IllegalArgumentException("Automóvil no encontrado con id " + matriculauto);
        }
    }
}
