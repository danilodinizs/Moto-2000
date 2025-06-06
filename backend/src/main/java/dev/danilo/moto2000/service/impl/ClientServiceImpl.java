package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.ServiceOrderDTO;
import dev.danilo.moto2000.dto.TransactionItemRequest;
import dev.danilo.moto2000.entity.Client;
import dev.danilo.moto2000.entity.ServiceOrder;
import dev.danilo.moto2000.entity.Transaction;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.MethodArgumentNotValidException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.ClientRepository;
import dev.danilo.moto2000.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ModelMapper mapper;

    private static final String CPF_REGEX = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";

    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches(CPF_REGEX);
    }

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

        if (!isCpfValido(dto.getCpf())) {
            Response badRequestResponse = Response.builder()
                    .status(400)
                    .message("O CPF deve estar no formato XXX.XXX.XXX-XX")
                    .build();

            throw new MethodArgumentNotValidException(badRequestResponse);
        }

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

        List<ClientDTO> clientsDTO = clients.stream().map(client -> mapper.map(client, ClientDTO.class)).collect(Collectors.toList());

        clientsDTO.forEach(client -> {
            if (client.getMotorcycles() != null) {
                client.setMotorcycles(null);
            }
        });
        clientsDTO.forEach(client -> {
            if (client.getTransactions() != null) {
                client.setTransactions(null);
            }
        });

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

        Client client = repository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        if (client.getMotorcycles() == null) {
            return Response.builder()
                    .status(404)
                    .message("Nenhuma motocicleta encontrada para o cliente: " + client.getName())
                    .build();
        }

        ClientDTO dto = mapper.map(client, ClientDTO.class);

        dto.setTransactions(null);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .motorcycles(dto.getMotorcycles())
                .build();
    }

    @Override
    public Response getTransactions(UUID id) {
        Client client = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (client.getTransactions() == null) {
            return Response.builder()
                    .status(404)
                    .message("Nenhuma transação encontrada para o cliente: " + client.getName())
                    .build();

        }


        ClientDTO dto = mapper.map(client, ClientDTO.class);

        dto.setMotorcycles(null);

        // TESTES - PROBLEMA ESTÁ NO MÉTODO SELL DE TRANSACTION SERIVCE IMPL

        List<TransactionItemRequest> transactionItemRequests = new ArrayList<>();

        transactionItemRequests.add(TransactionItemRequest.builder()
                .productId(client.getId())
                .quantity(2)
                .build());

        Set<UUID> productIds = new HashSet<>();

        productIds.add(client.getId());

        Set<ServiceOrderDTO> soDTO = new HashSet<>();

        soDTO.add(ServiceOrderDTO.builder()
                .name("jajaja")
                .description("bololo")
                .duration("2 minutos")
                .laborCost(BigDecimal.TEN)
                .createdAt(LocalDateTime.now())
                .productsIds(productIds)
                .build());

        dto.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setClient(null);
            transactionDTO.setItems(transactionItemRequests);
            transactionDTO.setServiceOrder(soDTO);
        });

        // TESTES

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .transactions(dto.getTransactions())
                .build();
    }

    @Override
    public Response updateClient(UUID id, ClientDTO clientDTO) {
        Client client = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        clientDTO.setId(client.getId());

        Client newClient = mapper.map(clientDTO, Client.class);

        repository.save(newClient);

        return Response.builder()
                .status(200)
                .message("Cliente atualizado com sucesso")
                .client(clientDTO)
                .build();
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        repository.delete(client);
    }

}
