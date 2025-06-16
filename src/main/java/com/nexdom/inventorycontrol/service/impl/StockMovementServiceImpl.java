package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.service.StockMovementService;
import org.springframework.beans.BeanUtils;
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
    public StockMovementModel registerStockMovement(StockMovementRecordDto stockMovementRecordDto) {
        StockMovementModel stockMovementModel = new StockMovementModel();
        BeanUtils.copyProperties(stockMovementRecordDto, stockMovementModel);
        ProductModel productModel = productService.findById(stockMovementRecordDto.productId()).get();
        stockMovementModel.setProduct(productModel);

        return stockMovementRepository.save(stockMovementModel);
    }

    @Override
    public Optional<StockMovementModel> findById(UUID stockMovementId) {
        Optional<StockMovementModel> stockMovementModelOptional = stockMovementRepository.findById(stockMovementId);
        if (stockMovementModelOptional.isEmpty()) {
            throw new NotFoundException("Stock Movement not found");
        }

        return stockMovementModelOptional;
    }

    @Transactional
    @Override
    public void delete(StockMovementModel stockMovementModel) {
        stockMovementRepository.delete(stockMovementModel);
    }
}
