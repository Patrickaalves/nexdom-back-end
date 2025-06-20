package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.model.SupplierModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface SupplierService {
    SupplierModel registerSupplier(SupplierRecordDto supplierRecordDto);

    Optional<SupplierModel> findById(UUID supplierId);

    Boolean existsByCode(String code);

    Page<SupplierModel> findAll(Specification<SupplierModel> spec, Pageable pageable);

    SupplierModel updateSupplier(SupplierRecordDto supplierRecordDto, SupplierModel supplierModel);

    void delete(SupplierModel supplierModel);

    SupplierModel findByIdModel(UUID uuid);
}
