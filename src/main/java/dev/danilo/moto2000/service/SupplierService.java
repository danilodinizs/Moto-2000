package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.SupplierDTO;

import java.util.UUID;

public interface SupplierService {
    Response addSupplier(SupplierDTO supplierDTO);
    Response updateSupplier(UUID id, SupplierDTO supplierDTO);
    Response getAllSuppliers();
    Response getSupplierById(UUID id);
    Response deleteSupplier(UUID id);
}
