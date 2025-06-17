package com.nexdom.inventorycontrol.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockMovementModelTest {

    @Test
    void prePersist_setsProductCode_whenProductIsNotNull() {
        ProductModel product = new ProductModel();
        product.setCode("PROD-123");

        StockMovementModel movement = new StockMovementModel();
        movement.setProduct(product);

        movement.prePersist();

        assertEquals("PROD-123", movement.getProductCode());
    }

    @Test
    void prePersist_doesNotThrow_whenProductIsNull() {
        StockMovementModel movement = new StockMovementModel();
        movement.setProduct(null);

        assertDoesNotThrow(movement::prePersist);
        assertNull(movement.getProductCode());
    }
}
