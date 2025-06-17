package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.model.StockMovementModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementResponseDto(
        UUID stockMovementId,
        UUID productId,
        OperationType operationType,
        BigDecimal salePrice,
        LocalDateTime saleDate,
        Integer movementQuantity,
        LocalDateTime creationDate,
        String productCode
) {
    public StockMovementResponseDto(StockMovementModel entity) {
        this(
                entity.getStockMovementId(),
                entity.getProduct() != null ? entity.getProduct().getProductId() : null,
                entity.getOperationType(),
                entity.getSalePrice(),
                entity.getSaleDate(),
                entity.getMovementQuantity(),
                entity.getCreationDate(),
                entity.getProductCode()
        );
    }
}