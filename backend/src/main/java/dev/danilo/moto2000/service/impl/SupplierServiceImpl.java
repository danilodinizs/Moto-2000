package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.SupplierDTO;
import dev.danilo.moto2000.entity.Category;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.MethodArgumentNotValidException;
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

    private static final String CNPJ_REGEX = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";

    private boolean isCnpjValido(String cnpj) {
        return cnpj != null && cnpj.matches(CNPJ_REGEX);
    }

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {

        if(!isCnpjValido(supplierDTO.getCnpj())) {
            Response badRequestResponse = Response.builder()
                    .status(400)
                    .message("O CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
                    .build();

            throw new MethodArgumentNotValidException(badRequestResponse);
        }

        if (repository.existsByCnpj(supplierDTO.getCnpj())) {
            Supplier existSupplier = repository.findByCnpj(supplierDTO.getCnpj());
            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("CNPJ " + supplierDTO.getCnpj() + " já cadastrado no sistema")
                    .supplier(mapper.map(existSupplier, SupplierDTO.class))
                    .build();
            throw new DataAlreadyExistsException(conflictResponse);
        }

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
    public void deleteSupplier(UUID id) {
        Supplier supplier = repository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrada"));

        repository.delete(supplier);

    }
}
