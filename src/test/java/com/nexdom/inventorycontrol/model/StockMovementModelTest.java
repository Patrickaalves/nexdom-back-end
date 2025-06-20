package com.nexdom.inventorycontrol.model;

import com.nexdom.inventorycontrol.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockMovementModelTest {

    private StockMovementModel stockMovement;
    private ProductModel product;

    @BeforeEach
    void setUp() {
        stockMovement = new StockMovementModel();
        product = new ProductModel();
        product.setCode("PROD001");

        stockMovement.setProduct(product);
        stockMovement.setSaleDate(LocalDateTime.now());
        stockMovement.setSalePrice(BigDecimal.valueOf(50.0));
        stockMovement.setMovementQuantity(10);
        stockMovement.setOperationType(OperationType.ENTRY);
    }

    @Test
    void testPrePersist_shouldSetProductCode() {
        stockMovement.prePersist();
        assertEquals("PROD001", stockMovement.getProductCode());
    }

    @Test
    void testPrePersist_withNullProduct_shouldNotThrow() {
        stockMovement.setProduct(null);
        assertDoesNotThrow(() -> stockMovement.prePersist());
        assertNull(stockMovement.getProductCode());
    }

    @Test
    void testSettersAndGetters() {
        UUID id = UUID.randomUUID();
        stockMovement.setStockMovementId(id);
        assertEquals(id, stockMovement.getStockMovementId());

        stockMovement.setCustomer(new CustomerModel());
        assertNotNull(stockMovement.getCustomer());

        stockMovement.setSupplier(new SupplierModel());
        assertNotNull(stockMovement.getSupplier());
    }
}
