package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.ProductType;

import java.util.UUID;

public record ProductAggregateResponseDto(
        UUID productId,
        String productCode,
        ProductType productType,
        Long quantityOut
) {}
