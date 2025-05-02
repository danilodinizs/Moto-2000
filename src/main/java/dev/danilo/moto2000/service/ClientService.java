package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;

import java.util.UUID;

public interface ClientService {

    Response saveClient(ClientDTO clientDTO);
    Response getAllClients();
    Response getByField(String field, String value);
    Response getMotorcycles(UUID id);
    Response getTransactions(UUID id);
    Response updateClient(UUID id, ClientDTO clientDTO);
    Response deleteClient(UUID id);

}
