package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.MotorcycleDTO;
import dev.danilo.moto2000.dto.Response;

import java.util.UUID;

public interface MotorcycleService {

    Response saveMotorcycle(MotorcycleDTO motorcycleDTO);
    Response getAllMotorcycle();
    Response getMotorcycleById(UUID id);
    Response updateMotorcycle(UUID id, MotorcycleDTO motorcycleDTO);
    void deleteMotorcycle(UUID id);
}
