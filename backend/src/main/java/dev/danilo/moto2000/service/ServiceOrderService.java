package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.ServiceOrderDTO;

import java.util.UUID;

public interface ServiceOrderService {
    Response saveServiceOrder(ServiceOrderDTO serviceOrderDTO);
    Response updateServiceOrder(UUID id, ServiceOrderDTO serviceOrderDTO);
    Response getAllServiceOrders();
    Response getServiceOrderById(UUID id);
    void deleteServiceOrder(UUID id);
}
