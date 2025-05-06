package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.SupplierDTO;
import dev.danilo.moto2000.entity.Category;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.SupplierRepository;
import dev.danilo.moto2000.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;

    private final ModelMapper mapper;

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = mapper.map(supplierDTO, Supplier.class);

        repository.save(supplier);

        return Response.builder()
                .status(200)
                .message("Fornecedor cadastrado com sucesso")
                .build();
    }

    @Override
    public Response updateSupplier(UUID id, SupplierDTO supplierDTO) {
        Supplier supplier = repository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrada"));

        if (repository.existsByCnpj(supplierDTO.getCnpj())) {
            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("CNPJ " + supplierDTO.getCnpj() + " já cadastrado no sistema")
                    .build();
            throw new DataAlreadyExistsException(conflictResponse);
        }

        supplierDTO.setId(supplier.getId());

        Supplier newSupplier = mapper.map(supplierDTO, Supplier.class);

        repository.save(newSupplier);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = repository.findAll();
        List<SupplierDTO> suppliersDTO = suppliers.stream().map(supplier -> mapper.map(supplier, SupplierDTO.class)).collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .suppliers(suppliersDTO)
                .build();
    }

    @Override
    public Response getSupplierById(UUID id) {
        Supplier supplier = repository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrada"));

        SupplierDTO dto = mapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .supplier(dto)
                .build();
    }

    @Override
    public Response deleteSupplier(UUID id) {
        Supplier supplier = repository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrada"));

        repository.delete(supplier);

        return Response.builder()
                .status(204)
                .message("Fornecedor deletado com sucesso")
                .build();
    }
}
