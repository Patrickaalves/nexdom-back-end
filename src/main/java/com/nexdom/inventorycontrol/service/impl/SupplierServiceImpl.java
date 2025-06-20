package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.exceptions.BusinessRuleException;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.SupplierModel;
import com.nexdom.inventorycontrol.repositories.SupplierRepository;
import com.nexdom.inventorycontrol.service.StockMovementService;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
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
    final StockMovementService stockMovementService;

    public SupplierServiceImpl(SupplierRepository supplierRepository, @Lazy StockMovementService stockMovementService) {
        this.supplierRepository = supplierRepository;
        this.stockMovementService = stockMovementService;
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
            throw new NotFoundException("Fornecedor não encontrado");
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
        if (stockMovementService.existSupplier(supplierModel)) {
            throw new BusinessRuleException("Existe movimento de estoque para esse fornecedor");
        }
        supplierRepository.delete(supplierModel);
    }

    @Override
    public SupplierModel findByIdModel(UUID uuid) {
        return supplierRepository.findById(uuid).orElse(null);
    }

    @Transactional
    @Override
    public SupplierModel updateSupplier(SupplierRecordDto supplierRecordDto, SupplierModel supplierModel) {
        supplierModel.setCnpj(supplierRecordDto.cnpj());
        supplierModel.setCode(supplierRecordDto.code());
        supplierModel.setName(supplierRecordDto.name());
        supplierModel.setPhone(supplierRecordDto.phone());

        return supplierRepository.save(supplierModel);
    }
}
