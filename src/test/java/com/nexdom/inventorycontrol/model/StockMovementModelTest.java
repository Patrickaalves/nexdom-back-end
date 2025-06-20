package com.nexdom.inventorycontrol.model;

import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StockMovementModelTest {

    private StockMovementModel movement;
    private ProductModel product;

    @BeforeEach
    void setUp() {
        product = new ProductModel();
        product.setCode("ABC123");

        movement = new StockMovementModel();
        movement.setProduct(product);
        movement.setSalePrice(BigDecimal.valueOf(100));
        movement.setSaleDate(LocalDateTime.now());
        movement.setMovementQuantity(2);
    }

    @Test
    void entryWithoutCostPrice_ok() {
        movement.setOperationType(OperationType.ENTRY);
        assertDoesNotThrow(movement::prePersist);
        assertEquals("ABC123", movement.getProductCode());
    }

    @Test
    void exitWithoutCostPrice_throws() {
        movement.setOperationType(OperationType.EXIT);
        assertThrows(BusinessRuleException.class, movement::prePersist);
    }

    @Test
    void exitWithCostPrice_ok() {
        movement.setOperationType(OperationType.EXIT);
        movement.setCostPrice(BigDecimal.valueOf(70));
        assertDoesNotThrow(movement::prePersist);
    }
}