package com.nexdom.inventorycontrol.model;

import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.exceptions.BusinessInsuficientStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    private ProductModel product;

    @BeforeEach
    void setUp() {
        product = new ProductModel();
        product.setStockQuantity(10);
        product.setCode("PROD123");
        product.setSupplierPrice(BigDecimal.valueOf(100.00));
        product.setProductType(ProductType.ELETRONIC);
    }

    @Test
    void testCreditStock_withValidQuantity_shouldIncreaseStock() {
        product.creditStock(5);
        assertEquals(15, product.getStockQuantity());
    }

    @Test
    void testCreditStock_withZero_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.creditStock(0);
        });
        assertEquals("Quantidade tem que ser positiva", exception.getMessage());
    }

    @Test
    void testCreditStock_withNegative_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.creditStock(-3);
        });
        assertEquals("Quantidade tem que ser positiva", exception.getMessage());
    }

    @Test
    void testDebitStock_withValidQuantity_shouldDecreaseStock() {
        product.debitStock(4);
        assertEquals(6, product.getStockQuantity());
    }

    @Test
    void testDebitStock_withZero_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.debitStock(0);
        });
        assertEquals("Quantidade tem que ser positiva", exception.getMessage());
    }

    @Test
    void testDebitStock_withNegative_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.debitStock(-5);
        });
        assertEquals("Quantidade tem que ser positiva", exception.getMessage());
    }

    @Test
    void testDebitStock_withGreaterThanStock_shouldThrowBusinessException() {
        BusinessInsuficientStock exception = assertThrows(BusinessInsuficientStock.class, () -> {
            product.debitStock(20);
        });
        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }
}
