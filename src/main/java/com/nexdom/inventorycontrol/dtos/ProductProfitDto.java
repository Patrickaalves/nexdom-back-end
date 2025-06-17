package com.nexdom.inventorycontrol.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductProfitDto(UUID productId,
                               String productCode,
                               Long totalQuantitySold,
                               BigDecimal totalProfit) {
}
