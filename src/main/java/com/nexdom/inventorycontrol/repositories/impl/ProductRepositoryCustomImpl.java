package com.nexdom.inventorycontrol.repositories.impl;

import com.nexdom.inventorycontrol.dtos.response.ProductProfitDto;
import com.nexdom.inventorycontrol.dtos.response.ProductAggregateDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.repositories.JpqlExecutor;
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
    public ProductProfitDto findProductProfits(UUID productId) {
        String jpqlStr = """
            SELECT NEW %s (
                p.productId,
                p.code,
                SUM(sm.movementQuantity),
                SUM((sm.salePrice - p.supplierPrice) * sm.movementQuantity))
            FROM StockMovementModel sm
                JOIN sm.product p
            WHERE sm.operationType = :opType
                AND p.productId = :productId
            GROUP BY p.productId, p.code
        """.formatted(ProductProfitDto.class.getName());

        Map<String, Object> filters = new HashMap<>();
        filters.put("opType", OperationType.EXIT);
        filters.put("productId", productId);

        return jpql.querySingle(jpqlStr, filters, ProductProfitDto.class);
    }

    @Override
    public ProductAggregateDto findProductsWithQuantitiesByType(String productType, UUID productId) {
        String jpqlQuery = """
            SELECT NEW %s (
                p.productId,
                p.code,
                p.productType,
                COALESCE(SUM(
                    CASE
                        WHEN sm.operationType = :operationType
                        THEN sm.movementQuantity
                        ELSE 0
                    END
                ), 0),
                p.stockQuantity
            )
            FROM ProductModel p
            LEFT JOIN p.stockMovement sm
            WHERE p.productType = :productType
              AND p.productId   = :productId
            GROUP BY p.productId, p.code, p.productType, p.stockQuantity
        """.formatted(ProductAggregateDto.class.getName());

        Map<String, Object> filters = new HashMap<>();
        filters.put("productType", ProductType.valueOf(productType));
        filters.put("productId", productId);
        filters.put("operationType", OperationType.EXIT);

        return jpql.querySingle(jpqlQuery, filters, ProductAggregateDto.class);
    }
}

