package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.StockMovementRecordDto;
import com.nexdom.inventorycontrol.dtos.response.StockMovementResponseDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.service.StockMovementService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class StockMovementServiceImpl implements StockMovementService {

    final StockMovementRepository stockMovementRepository;
    final ProductService productService;

    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository, ProductService productService) {
        this.stockMovementRepository = stockMovementRepository;
        this.productService = productService;
    }

    @Transactional
    @Override
    public StockMovementResponseDto registerStockMovement(StockMovementRecordDto dto) {

        ProductModel product = productService.findById(dto.productId()).get();

        updateProductStock(product, dto);

        StockMovementModel mov = createStockMovement(dto, product);

        stockMovementRepository.save(mov);

        return new StockMovementResponseDto(mov);
    }

    private void updateProductStock(ProductModel product, StockMovementRecordDto dto) {
        if (dto.operationType() == OperationType.EXIT) {
            validateStockAvailability(product, dto.movementQuantity());
            product.setStockQuantity(product.getStockQuantity() - dto.movementQuantity());
        } else {
            product.setStockQuantity(product.getStockQuantity() + dto.movementQuantity());
        }
    }

    private void validateStockAvailability(ProductModel product, Integer quantity) {
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for exit");
        }
    }

    private StockMovementModel createStockMovement(StockMovementRecordDto dto, ProductModel product) {
        StockMovementModel mov = new StockMovementModel();
        BeanUtils.copyProperties(dto, mov);
        mov.setProduct(product);
        return mov;
    }

    @Override
    public Optional<StockMovementModel> findById(UUID stockMovementId) {
        Optional<StockMovementModel> stockMovementModelOptional = stockMovementRepository.findById(stockMovementId);
        if (stockMovementModelOptional.isEmpty()) {
            throw new NotFoundException("Stock Movement not found");
        }

        return stockMovementModelOptional;
    }

    @Override
    public StockMovementResponseDto findByIdDto(UUID stockMovementId) {
        return this.findById(stockMovementId).map(StockMovementResponseDto::new).get();
    }

    @Transactional
    @Override
    public void delete(StockMovementModel stockMovementModel) {
        stockMovementRepository.delete(stockMovementModel);
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
