package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.MotorcycleDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.entity.Motorcycle;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.MethodArgumentNotValidException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.MotorcycleRepository;
import dev.danilo.moto2000.service.MotorcycleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MotorcycleServiceImpl implements MotorcycleService {

    private final MotorcycleRepository repository;

    private final ModelMapper mapper;

    private static final String PLACA_REGEX = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$";

    private boolean isPlacaValida(String placa) {
        return placa != null && placa.matches(PLACA_REGEX);
    }

    @Override
    public Response saveMotorcycle(MotorcycleDTO motorcycleDTO) {

        if (!isPlacaValida(motorcycleDTO.getLicensePlate())) {
            Response badRequestResponse = Response.builder()
                    .status(400)
                    .message("A placa deve estar no formato ABC-1234 ou ABC1D23")
                    .build();

            throw new MethodArgumentNotValidException(badRequestResponse);
        }

        if(repository.existsByLicensePlate(motorcycleDTO.getLicensePlate())) {
            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("Placa " + motorcycleDTO.getLicensePlate() + " já cadastrada no sitema")
                    .motorcycle(motorcycleDTO)
                    .build();
            throw new DataAlreadyExistsException(conflictResponse);
        }

        repository.save(mapper.map(motorcycleDTO, Motorcycle.class));

        return Response.builder()
                .status(200)
                .message("Motocicleta cadastrada com sucesso")
                .build();
    }

    @Override
    public Response getAllMotorcycle() {

        List<Motorcycle> motorcycles = repository.findAll();

        List<MotorcycleDTO> motorcyclesDTO = motorcycles.stream().map(motorcycle -> mapper.map(motorcycle, MotorcycleDTO.class)).collect(Collectors.toList());

        motorcycles.forEach(motorcycle -> {
            motorcycle.setClient(null);
        });
        
        return Response.builder()
                .status(200)
                .message("Sucesso")
                .motorcycles(motorcyclesDTO)
                .build()
                ;
    }

    @Override
    public Response getMotorcycleById(UUID id) {
        Motorcycle motorcycle = repository.findById(id).orElseThrow(() -> new NotFoundException("Motocicleta não encontrada"));

        motorcycle.setClient(null);

        MotorcycleDTO dto = mapper.map(motorcycle, MotorcycleDTO.class);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .motorcycle(dto)
                .build();
    }

    @Override
    public Response updateMotorcycle(UUID id, MotorcycleDTO motorcycleDTO) {
        Motorcycle motorcycle = repository.findById(id).orElseThrow(() -> new NotFoundException("Motocicleta não encontrada"));

        motorcycleDTO.setId(motorcycle.getId());

        Motorcycle newMotorcycle = mapper.map(motorcycleDTO, Motorcycle.class);

        repository.save(newMotorcycle);

        return Response.builder()
                .status(200)
                .message("Motocicleta atualizada com sucesso")
                .motorcycle(motorcycleDTO)
                .build();
    }

    @Override
    public void deleteMotorcycle(UUID id) {
        Motorcycle motorcycle = repository.findById(id).orElseThrow(() -> new NotFoundException("Motocicleta não encontrada"));

        repository.delete(motorcycle);

    }
}
