package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.dtos.ProductProfitDto;
import com.nexdom.inventorycontrol.dtos.ProductAggregateDto;

import java.util.UUID;

public interface ProductRepositoryCustom {
    ProductProfitDto findProductProfits(UUID productId);

    ProductAggregateDto findProductsWithQuantitiesByType(String productType, UUID productId);
}
