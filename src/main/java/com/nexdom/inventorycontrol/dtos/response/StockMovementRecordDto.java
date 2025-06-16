package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.OperationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementRecordDto(@NotNull(message = "productId is mandatory", 
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     UUID productId,

                                     @NotNull(message = "operationType is mandatory",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     OperationType operationType,

                                     @NotNull(message = "salePrice is mandatory",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     @Positive(message = "salePrice must be greater than zero",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     @Digits(integer = 10, fraction = 2,
                                             message = "salePrice supports max 10 integer digits and 2 decimals",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     BigDecimal salePrice,

                                     @NotNull(message = "saleDate is mandatory",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     @PastOrPresent(message = "saleDate cannot be in the future",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     LocalDateTime saleDate,

                                     @NotNull(message = "movementQuantity is mandatory",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     @Positive(message = "movementQuantity must be greater than zero",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     @Max(value = 1_000_000, message = "movementQuantity above the allowed limit",
                                             groups = {StockMovementRecordDto.stockMovementView.stockMovementPost.class, StockMovementRecordDto.stockMovementView.stockMovementPut.class})
                                     Integer movementQuantity){
    public interface stockMovementView {
        interface stockMovementPost {}
        interface stockMovementPut {}
    }
}
