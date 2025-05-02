package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;

import java.util.UUID;

public interface ClientService {

    Response saveClient(ClientDTO clientDTO);
    Response getAllClients();
    Response getClientByName(String name);
    Response getClientByCpf(String cpf);
    Response getClientByEmail(String email);
    Response getClientByPhoneNumber(String phoneNumber);
    Response getMotorcycles(UUID id);
    Response getTransactions(UUID id);
    Response updateClient(UUID id, ClientDTO clientDTO);
    Response deleteClient(UUID id);

}
