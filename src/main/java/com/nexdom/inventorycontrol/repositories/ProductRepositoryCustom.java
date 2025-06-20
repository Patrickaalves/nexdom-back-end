package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.dtos.response.ProductProfitResponseDto;
import com.nexdom.inventorycontrol.dtos.response.ProductAggregateResponseDto;

import java.util.UUID;

public interface ProductRepositoryCustom {
    ProductProfitResponseDto findProductProfits(UUID productId);

    ProductAggregateResponseDto findProductsWithQuantitiesByType(UUID productId);
}
