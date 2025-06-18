package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.response.SupplierRecordDto;
import com.nexdom.inventorycontrol.model.SupplierModel;

import java.util.Optional;
import java.util.UUID;

public interface SupplierService {
    SupplierModel registerSupplier(SupplierRecordDto supplierRecordDto);

    Optional<SupplierModel> findById(UUID supplierId);
}
