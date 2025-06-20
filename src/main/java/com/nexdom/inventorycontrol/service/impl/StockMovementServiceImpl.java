package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.StockMovementRecordDto;
import com.nexdom.inventorycontrol.dtos.response.StockMovementResponseDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.BusinessRuleException;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.CustomerModel;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.model.SupplierModel;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import com.nexdom.inventorycontrol.service.CustomerService;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.service.StockMovementService;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockMovementServiceImpl implements StockMovementService {

    final StockMovementRepository stockMovementRepository;
    final CustomerService customerService;
    final ProductService productService;
    final SupplierService supplierService;

    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository, @Lazy CustomerService customerService, @Lazy ProductService productService, SupplierService supplierService) {
        this.stockMovementRepository = stockMovementRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.supplierService = supplierService;
    }

    @Transactional
    @Override
    public StockMovementModel registerStockMovement(StockMovementRecordDto dto) {

        ProductModel product = productService.findById(dto.productId()).get();

        updateProductStockForRegister(product, dto);

        StockMovementModel stockMovement = createStockMovement(dto, product);

        return stockMovementRepository.save(stockMovement);
    }

    private void updateProductStockForRegister(ProductModel product,
                                               StockMovementRecordDto dto) {

        boolean isEntry = dto.operationType() == OperationType.ENTRY;
        boolean isExit  = dto.operationType() == OperationType.EXIT;

        if (isEntry && dto.supplierId() == null) {
            throw new BusinessRuleException("Somente um FORNECEDOR pode registrar movimento de ENTRADA.");
        }
        if (isExit && dto.customerId() == null) {
            throw new BusinessRuleException("Somente um CLIENTE pode registrar movimento de SAÍDA.");
        }

        switch (dto.operationType()) {
            case ENTRY -> {
                product.creditStock(dto.movementQuantity());
                product.setSupplierPrice(dto.salePrice());
            }
            case EXIT  -> product.debitStock(dto.movementQuantity());
        }
    }

    private StockMovementModel createStockMovement(StockMovementRecordDto dto, ProductModel product) {
        StockMovementModel mov = new StockMovementModel();
        BeanUtils.copyProperties(dto, mov);
        mov.setProduct(product);

        if (dto.operationType() == OperationType.ENTRY) {
            mov.setCostPrice(dto.salePrice());
        } else if (dto.operationType() == OperationType.EXIT) {
            BigDecimal currentCost = product.getSupplierPrice() != null
                    ? product.getSupplierPrice()
                    : BigDecimal.ZERO;
            mov.setCostPrice(currentCost);
        }

        if (dto.customerId() != null) {
            CustomerModel customerModel = customerService.findById(dto.customerId()).get();
            if (customerModel != null) {
                mov.setCustomer(customerModel);
            }
        }

        if (dto.supplierId() != null) {
            SupplierModel supplierModel = supplierService.findById(dto.supplierId()).get();
            if (supplierModel != null) {
                mov.setSupplier(supplierModel);
            }
        }

        return mov;
    }

    @Override
    public Optional<StockMovementModel> findById(UUID stockMovementId) {
        Optional<StockMovementModel> stockMovementModelOptional = stockMovementRepository.findById(stockMovementId);
        if (stockMovementModelOptional.isEmpty()) {
            throw new NotFoundException("Movimento de estoque não encontrado");
        }

        return stockMovementModelOptional;
    }

    @Override
    public StockMovementResponseDto findByIdDto(UUID stockMovementId) {
        return this.findById(stockMovementId).map(StockMovementResponseDto::new).get();
    }

    @Override
    public Boolean existsByProductId(UUID productId) {
        return stockMovementRepository.existsByProductId(productId);
    }

    @Override
    public boolean existCustomer(CustomerModel customerModel) {
        return stockMovementRepository.existsByCustomer(customerModel);
    }

    @Transactional
    @Override
    public void delete(StockMovementModel stockMovementModel) {
        UUID supplierId = stockMovementModel.getSupplier() != null ? stockMovementModel.getSupplier().getSupplierId() : null ;
        UUID customerId = stockMovementModel.getCustomer() != null ? stockMovementModel.getCustomer().getCustomerId() : null ;
        updateProductStockForDelete(stockMovementModel.getProduct(), stockMovementModel.getOperationType(), stockMovementModel.getMovementQuantity(),
                supplierId, customerId);
        stockMovementRepository.delete(stockMovementModel);
    }

    private void updateProductStockForDelete(ProductModel product,
                                             OperationType operationType,
                                             Integer movementQuantity,
                                             UUID supplierId,
                                             UUID customerId) {

        boolean isEntry = operationType == OperationType.ENTRY;
        boolean isExit  = operationType == OperationType.EXIT;

        if (isEntry && supplierId == null) {
            throw new BusinessRuleException(
                    "Somente um FORNECEDOR pode excluir um movimento de ENTRADA.");
        }
        if (isExit && customerId == null) {
            throw new BusinessRuleException(
                    "Somente um CLIENTE pode excluir um movimento de SAÍDA.");
        }

        switch (operationType) {
            case ENTRY -> product.debitStock(movementQuantity);
            case EXIT  -> product.creditStock(movementQuantity);
        }
    }

    @Override
    public Page<StockMovementModel> findAll(Specification<StockMovementModel> spec, Pageable pageable) {
        return stockMovementRepository.findAll(spec, pageable);
    }

    @Override
    public Page<StockMovementResponseDto> findAllDto(Specification<StockMovementModel> spec, Pageable pageable) {
        Page<StockMovementModel> stockMovementModel = stockMovementRepository.findAll(spec, pageable);

        return stockMovementModel.map(StockMovementResponseDto::new);
    }
}