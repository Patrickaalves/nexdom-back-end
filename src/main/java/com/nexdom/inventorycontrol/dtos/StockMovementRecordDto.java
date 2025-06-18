package com.nexdom.inventorycontrol.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.enums.OperationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementRecordDto(@JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "ID do produto é obrigatório",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     UUID productId,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     OperationType operationType,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "Preço de venda é obrigatório",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Positive(message = "Preço de venda deve ser maior que zero",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Digits(integer = 10, fraction = 2,
                                             message = "Preço de venda suporta no maximo 10 digitos e 2 decimais",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     BigDecimal salePrice,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "Data de venda é obrigatório",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @PastOrPresent(message = "Data de venda nao pode ser uma data futura",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     LocalDateTime saleDate,

                                     @JsonView(stockMovementView.stockMovementPost.class)
                                     @NotNull(message = "Quantidade movimentada é obrigatória",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Positive(message = "Quantidade movimentada deve ser maior que zero",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     @Max(value = 1_000_000, message = "Quantidade movimentada acima do limite permitido",
                                             groups = StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                     Integer movementQuantity){
    public interface stockMovementView {
        interface stockMovementPost {}
    }
}
