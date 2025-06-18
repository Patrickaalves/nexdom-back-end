package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.SupplierModel;
import com.nexdom.inventorycontrol.repositories.SupplierRepository;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierServiceImpl implements SupplierService {
    final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierModel registerSupplier(SupplierRecordDto supplierRecordDto) {
        SupplierModel supplierModel = new SupplierModel();
        BeanUtils.copyProperties(supplierRecordDto, supplierModel);
        return supplierRepository.save(supplierModel);
    }

    @Override
    public Optional<SupplierModel> findById(UUID supplierId) {
        Optional<SupplierModel> supplierModelOptional = supplierRepository.findById(supplierId);
        if (supplierModelOptional.isEmpty()) {
            throw new NotFoundException("Vendedor não encontrado");
        }

        return supplierModelOptional;
    }
}
