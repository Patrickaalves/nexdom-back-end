package com.nexdom.inventorycontrol.model;

import com.nexdom.inventorycontrol.exceptions.BusinessInsuficientStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    private ProductModel product;

    @BeforeEach
    void setUp() {
        product = new ProductModel();
        product.setStockQuantity(10);
        product.setSupplierPrice(BigDecimal.valueOf(50));
    }

    //  creditStock

    @Test
    @DisplayName("creditStock deve somar quantidade ao estoque quando quantidade é positiva")
    void creditStock_PositiveQuantity_IncreasesStock() {
        product.creditStock(5);
        assertEquals(15, product.getStockQuantity());
    }

    @Test
    @DisplayName("creditStock deve lançar IllegalArgumentException quando quantidade ≤ 0")
    void creditStock_NonPositiveQuantity_ThrowsException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> product.creditStock(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> product.creditStock(-3))
        );
    }

    //  debitStock 

    @Test
    @DisplayName("debitStock deve subtrair quantidade do estoque quando há estoque suficiente")
    void debitStock_SufficientStock_DecreasesStock() {
        product.debitStock(3);
        assertEquals(7, product.getStockQuantity());
    }

    @Test
    @DisplayName("debitStock deve lançar BusinessInsuficientStock quando estoque é insuficiente")
    void debitStock_InsufficientStock_ThrowsBusinessException() {
        assertThrows(BusinessInsuficientStock.class, () -> product.debitStock(15));
    }

    @Test
    @DisplayName("debitStock deve lançar IllegalArgumentException quando quantidade ≤ 0")
    void debitStock_NonPositiveQuantity_ThrowsException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> product.debitStock(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> product.debitStock(-2))
        );
    }
}
