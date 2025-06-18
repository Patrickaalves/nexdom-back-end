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
        UUID customerId,
        LocalDateTime creationDate,
        String productCode
) {
    public StockMovementResponseDto(StockMovementModel stockMovement) {
        this(
                stockMovement.getStockMovementId(),
                stockMovement.getProduct() != null ? stockMovement.getProduct().getProductId() : null,
                stockMovement.getOperationType(),
                stockMovement.getSalePrice(),
                stockMovement.getSaleDate(),
                stockMovement.getMovementQuantity(),
                stockMovement.getCustomer().getCustomerId(),
                stockMovement.getCreationDate(),
                stockMovement.getProductCode()
        );
    }
}