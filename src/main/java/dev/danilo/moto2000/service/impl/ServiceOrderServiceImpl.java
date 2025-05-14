package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.ServiceOrderDTO;
import dev.danilo.moto2000.entity.Product;
import dev.danilo.moto2000.entity.ServiceOrder;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.ProductRepository;
import dev.danilo.moto2000.repository.ServiceOrderRepository;
import dev.danilo.moto2000.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository repository;

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    @Override
    public Response saveServiceOrder(ServiceOrderDTO dto) {
        ServiceOrder so = mapper.map(dto, ServiceOrder.class);

        Set<Product> products = new HashSet<>(productRepository.findAllById(dto.getProductsIds()));

        if (products.size() != dto.getProductsIds().size()) {
            throw new RuntimeException("Um ou mais produtos não encontrados!");
        }

        so.setProducts(products);

        repository.save(so);

        return Response.builder()
                .status(200)
                .message("Ordem de serviço criada com sucesso")
                .serviceOrder(dto)
                .build();
    }

    @Override
    public Response updateServiceOrder(UUID id, ServiceOrderDTO dto) {
        ServiceOrder so = repository.findById(id).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        dto.setId(so.getId());

        Set<Product> products = new HashSet<>(productRepository.findAllById(dto.getProductsIds()));

        if (products.size() != dto.getProductsIds().size()) {
            throw new RuntimeException("Um ou mais produtos não encontrados!");
        }

        ServiceOrder newSo = mapper.map(dto, ServiceOrder.class);

        newSo.setProducts(products);

        repository.save(newSo);

        return Response.builder()
                .status(200)
                .message("Ordem de serviço atualizada com sucesso")
                .serviceOrder(dto)
                .build();
    }

    @Override
    public Response getAllServiceOrders() {
        List<ServiceOrder> soList = repository.findAll();

        List<ServiceOrderDTO> soListDTO = soList.stream().map(so -> mapper.map(so, ServiceOrderDTO.class)).toList();

        Set<UUID> productsIds = new HashSet<>();

        soList.forEach(so -> {
            so.getProducts().forEach(product -> {
                productsIds.add(product.getId());
            });
        });

        soListDTO.forEach(so -> {
            so.setProductsIds(productsIds);
        });

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .serviceOrders(soListDTO)
                .build();
    }

    @Override
    public Response getServiceOrderById(UUID id) {
        ServiceOrder so = repository.findById(id).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        Set<UUID> productsIds = new HashSet<>();

        so.getProducts().forEach(product -> {
            productsIds.add(product.getId());
        });

        ServiceOrderDTO soDTO = mapper.map(so, ServiceOrderDTO.class);

        soDTO.setProductsIds(productsIds);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .serviceOrder(soDTO)
                .build();
    }

    @Override
    public void deleteServiceOrder(UUID id) {
        ServiceOrder so = repository.findById(id).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        repository.delete(so);
    }
}
