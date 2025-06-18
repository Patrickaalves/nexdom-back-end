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
    public StockMovementModel registerStockMovement(StockMovementRecordDto dto) {

        ProductModel product = productService.findById(dto.productId()).get();

        updateProductStockForRegister(product, dto);

        StockMovementModel stockMovement = createStockMovement(dto, product);

        return stockMovementRepository.save(stockMovement);
    }

    private void updateProductStockForRegister(ProductModel product, StockMovementRecordDto dto) {
        switch (dto.operationType()) {
            case EXIT  -> product.debitStock(dto.movementQuantity());
            case ENTRY -> product.creditStock(dto.movementQuantity());
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
            throw new NotFoundException("Movimento de estoque não encontrado");
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
        updateProductStockForDelete(stockMovementModel.getProduct(), stockMovementModel.getOperationType(), stockMovementModel.getMovementQuantity());
        stockMovementRepository.delete(stockMovementModel);
    }

    private void updateProductStockForDelete(ProductModel product,OperationType operationType, Integer movementQuantity) {
        switch (operationType) {
            case EXIT  -> product.creditStock(movementQuantity);
            case ENTRY -> product.debitStock(movementQuantity);
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
