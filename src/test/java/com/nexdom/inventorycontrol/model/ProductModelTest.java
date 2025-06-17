package com.nexdom.inventorycontrol.model;

import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.exceptions.BusinessInsuficientStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    private ProductModel newProduct(int initialQty) {
        ProductModel p = new ProductModel();
        p.setCode("ABC");
        p.setProductType(ProductType.ELETRONIC);
        p.setSupplierPrice(BigDecimal.TEN);
        p.setStockQuantity(initialQty);
        return p;
    }

    /* ----------------------------------------------------------
       creditStock
       ---------------------------------------------------------- */
    @Nested
    @DisplayName("creditStock()")
    class Credit {

        @Test
        void addsQuantity_whenPositive() {
            ProductModel product = newProduct(5);

            product.creditStock(3);

            assertEquals(8, product.getStockQuantity());
        }

        @Test
        void throwsException_whenZeroOrNegative() {
            ProductModel product = newProduct(5);

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> product.creditStock(0)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> product.creditStock(-2))
            );
        }
    }

    /* ----------------------------------------------------------
       debitStock
       ---------------------------------------------------------- */
    @Nested
    @DisplayName("debitStock()")
    class Debit {

        @Test
        void subtractsQuantity_whenEnoughStock() {
            ProductModel product = newProduct(10);

            product.debitStock(4);

            assertEquals(6, product.getStockQuantity());
        }

        @Test
        void throwsBusinessInsuficientStock_whenNotEnough() {
            ProductModel product = newProduct(3);

            BusinessInsuficientStock ex = assertThrows(
                    BusinessInsuficientStock.class,
                    () -> product.debitStock(5));

            assertTrue(ex.getMessage().contains("requested 5, current stock 3"));
        }

        @Test
        void throwsIllegalArgument_whenZeroOrNegative() {
            ProductModel product = newProduct(4);

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> product.debitStock(0)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> product.debitStock(-1))
            );
        }
    }
}
