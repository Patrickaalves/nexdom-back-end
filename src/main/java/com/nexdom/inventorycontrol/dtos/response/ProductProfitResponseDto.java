package com.nexdom.inventorycontrol.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductProfitResponseDto(UUID productId,
                                       String productCode,
                                       Long totalQuantitySold,
                                       BigDecimal totalProfit) {
}
