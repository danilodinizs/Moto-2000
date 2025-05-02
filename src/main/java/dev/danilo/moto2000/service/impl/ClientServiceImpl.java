package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.entity.Client;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.ClientRepository;
import dev.danilo.moto2000.service.ClientService;
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
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ModelMapper mapper;

    private void validateUniqueFields(ClientDTO dto) {
        Map<String, Supplier<Boolean>> fieldChecks = Map.of(
                "CPF", () -> repository.existsByCpf(dto.getCpf()),
                "email", () -> repository.existsByEmail(dto.getEmail()),
                "telefone", () -> repository.existsByPhoneNumber(dto.getPhoneNumber())
        );

        fieldChecks.forEach((fieldName, check) -> {
            if (check.get()) {
                throw new DataAlreadyExistsException(
                        Response.builder()
                                .status(409)
                                .message(fieldName + " já cadastrado")
                                .build()
                );
            }
        });
    }

    @Override
    public Response saveClient(ClientDTO dto) {

        validateUniqueFields(dto);

        repository.save(mapper.map(dto, Client.class));

        return Response.builder()
                .status(200)
                .message("Cliente criado com sucesso")
                .build();
    }


    @Override
    public Response getAllClients() {

        List<Client> clients = repository.findAll();

        List<ClientDTO> clientsDTO = clients.stream().map(client -> mapper.map(clients, ClientDTO.class)).collect(Collectors.toList());

        clientsDTO.forEach(client -> client.setMotorcycles(null));
        clientsDTO.forEach(client -> client.setTransactions(null));

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .clients(clientsDTO)
                .build();
    }

    @Override
    public Response getByField(String field, String value) {
        Client client = switch (field.toLowerCase()) {
            case "cpf" -> repository.findByCpf(value).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
            case "email" -> repository.findByEmail(value).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
            case "telefone" -> repository.findByPhoneNumber(value).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
            default -> throw new IllegalArgumentException("Campo inválido");
        };

        return Response.builder()
                .status(200)
                .message("Cliente encontrado")
                .client(mapper.map(client, ClientDTO.class))
                .build();
    }

    @Override
    public Response getMotorcycles(UUID id) {
        return null;
    }

    @Override
    public Response getTransactions(UUID id) {
        return null;
    }

    @Override
    public Response updateClient(UUID id, ClientDTO clientDTO) {
        return null;
    }

    @Override
    public Response deleteClient(UUID id) {
        return null;
    }
}
