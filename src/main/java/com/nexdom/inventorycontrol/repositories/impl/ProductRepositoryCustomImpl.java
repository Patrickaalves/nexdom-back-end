package com.nexdom.inventorycontrol.repositories.impl;

import com.nexdom.inventorycontrol.dtos.response.ProductAggregateResponseDto;
import com.nexdom.inventorycontrol.dtos.response.ProductProfitResponseDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.repositories.ProductRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final JpqlExecutor jpql;

    public ProductRepositoryCustomImpl(JpqlExecutor jpql) {
        this.jpql = jpql;
    }

    @Override
    public ProductProfitResponseDto findProductProfits(UUID productId) {
        String jpqlStr = """
        SELECT NEW %s (
            p.productId,
            p.code,
            SUM(sm.movementQuantity),
            SUM(sm.salePrice * sm.movementQuantity),
            SUM((sm.salePrice - sm.costPrice) * sm.movementQuantity)
        )
        FROM StockMovementModel sm
        JOIN sm.product p
        WHERE sm.operationType = :entryOp
          AND p.productId = :productId
        GROUP BY p.productId, p.code
        """.formatted(ProductProfitResponseDto.class.getName());

        Map<String, Object> params = Map.of(
                "entryOp", OperationType.EXIT,
                "productId", productId
        );

        return jpql.querySingle(jpqlStr, params, ProductProfitResponseDto.class);
    }

    @Override
    public ProductAggregateResponseDto findProductsWithQuantitiesByType(UUID productId) {
        String jpqlQuery = """
        SELECT NEW %s (
            p.productId,
            p.code,
            p.productType,
            COALESCE(SUM(CASE WHEN sm.operationType = :exitOperationType THEN sm.movementQuantity ELSE 0 END), 0),
            p.stockQuantity
        )
        FROM ProductModel p
        LEFT JOIN p.stockMovement sm
        WHERE p.productId = :productId
        GROUP BY p.productId, p.code, p.productType, p.stockQuantity
        """.formatted(ProductAggregateResponseDto.class.getName());


        Map<String, Object> filters = new HashMap<>();
        filters.put("productId", productId);
        filters.put("exitOperationType", OperationType.EXIT);

        return jpql.querySingle(jpqlQuery, filters, ProductAggregateResponseDto.class);
    }
}

