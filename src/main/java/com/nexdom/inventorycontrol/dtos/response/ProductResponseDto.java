package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductResponseDto(UUID productId,
                                 String code,
                                 ProductType productType,
                                 BigDecimal supplierPrice,
                                 Integer stockQuantity,
                                 LocalDateTime creationDate,
                                 LocalDateTime lastUpdate,
                                 Set<UUID> stockmovements,
                                 UUID supplier
                                 ) {
    public ProductResponseDto(ProductModel entity) {
        this(
                entity.getProductId(),
                entity.getCode(),
                entity.getProductType(),
                entity.getSupplierPrice(),
                entity.getStockQuantity(),
                entity.getCreationDate(),
                entity.getLastUpdate(),
                entity.getStockMovement() != null
                        ? entity.getStockMovement().stream()
                            .map(StockMovementModel::getStockMovementId)
                            .collect(Collectors.toSet())
                        : Collections.emptySet(),
                entity.getSupplier().getSupplierId()
        );
    }
}
