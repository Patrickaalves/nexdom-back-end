package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.SupplierModel;
import com.nexdom.inventorycontrol.repositories.SupplierRepository;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierServiceImpl implements SupplierService {
    final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
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

    @Override
    public Boolean existsByCode(String code) {
        return supplierRepository.existsByCode(code);
    }

    @Override
    public Page<SupplierModel> findAll(Specification<SupplierModel> spec, Pageable pageable) {
        return supplierRepository.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public void delete(SupplierModel supplierModel) {
        supplierRepository.delete(supplierModel);
    }

    @Transactional
    @Override
    public SupplierModel updateSupplier(SupplierRecordDto supplierRecordDto, SupplierModel supplierModel) {
        supplierModel.setCnpj(supplierModel.getCnpj());
        supplierModel.setCode(supplierModel.getCode());
        supplierModel.setName(supplierModel.getName());
        supplierModel.setPhone(supplierModel.getPhone());
        return supplierRepository.save(supplierModel);
    }
}
