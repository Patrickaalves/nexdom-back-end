package com.nexdom.inventorycontrol.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.enums.OperationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementRecordDto(@JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "productId is mandatory",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     UUID productId,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     OperationType operationType,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "salePrice is mandatory",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Positive(message = "salePrice must be greater than zero",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Digits(integer = 10, fraction = 2,
                                             message = "salePrice supports max 10 integer digits and 2 decimals",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     BigDecimal salePrice,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "saleDate is mandatory",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @PastOrPresent(message = "saleDate cannot be in the future",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     LocalDateTime saleDate,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "movementQuantity is mandatory",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Positive(message = "movementQuantity must be greater than zero",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Max(value = 1_000_000, message = "movementQuantity above the allowed limit",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     Integer movementQuantity){
    public interface stockMovementView {
        interface stockMovementPost {}
    }
}
