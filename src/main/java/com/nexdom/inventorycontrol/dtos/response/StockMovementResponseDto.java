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
        BigDecimal costPrice,
        LocalDateTime saleDate,
        Integer movementQuantity,
        UUID customerId,
        LocalDateTime creationDate,
        String productCode,
        UUID supplierId
) {
    public StockMovementResponseDto(StockMovementModel stockMovement) {
        this(
                stockMovement.getStockMovementId(),
                stockMovement.getProduct() != null ? stockMovement.getProduct().getProductId() : null,
                stockMovement.getOperationType(),
                stockMovement.getSalePrice(),
                stockMovement.getCostPrice(),
                stockMovement.getSaleDate(),
                stockMovement.getMovementQuantity(),
                stockMovement.getCustomer() != null ? stockMovement.getCustomer().getCustomerId() : null,
                stockMovement.getCreationDate(),
                stockMovement.getProductCode(),
                stockMovement.getSupplier() != null ? stockMovement.getSupplier().getSupplierId() : null
        );
    }
}