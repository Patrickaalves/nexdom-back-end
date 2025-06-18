package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.dtos.response.ProductAggregateResponseDto;
import com.nexdom.inventorycontrol.dtos.response.ProductProfitResponseDto;
import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.exceptions.ProductMovementStockExist;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.repositories.ProductRepository;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    StockMovementRepository stockMovementRepository;

    @InjectMocks
    ProductServiceImpl service;

    UUID productId;
    ProductModel product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new ProductModel();
        product.setProductId(productId);
        product.setCode("ABC");
        product.setStockQuantity(10);
        product.setSupplierPrice(BigDecimal.valueOf(5));
    }

    /* ---------- registerProduct ---------- */

    @Test
    @DisplayName("registerProduct() deve salvar e retornar o novo produto")
    void registerProduct_savesProduct() {
        ProductRecordDto dto = new ProductRecordDto("ABC", ProductType.ELETRONIC, BigDecimal.valueOf(5), 10);

        when(productRepository.save(any(ProductModel.class))).thenAnswer(a -> a.getArgument(0));

        ProductModel productModelSaved = service.registerProduct(dto);

        ArgumentCaptor<ProductModel> captor = ArgumentCaptor.forClass(ProductModel.class);
        verify(productRepository).save(captor.capture());
        assertEquals("ABC", captor.getValue().getCode());
        assertEquals(productModelSaved, captor.getValue());
    }

    /* ---------- existsByCode ---------- */

    @Test
    void existsByCode_delegatesToRepository() {
        when(productRepository.existsByCode("XYZ")).thenReturn(true);
        assertTrue(service.existsByCode("XYZ"));
        verify(productRepository).existsByCode("XYZ");
    }

    /* ---------- findAll ---------- */

    @Test
    void findAll_delegatesToRepository() {
        Specification<ProductModel> spec = (root, query, cb) -> cb.conjunction();
        Page<ProductModel> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<ProductModel> result = service.findAll(spec, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    /* ---------- findById ---------- */

    @Nested
    class FindById {

        @Test
        void returnsProduct_whenExists() {
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            ProductModel result = service.findById(productId).orElseThrow();

            assertEquals(productId, result.getProductId());
        }

        @Test
        void throwsNotFound_whenMissing() {
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.findById(productId));
        }
    }

    /* ---------- delete ---------- */

    @Test
    void delete_callsRepositoryDelete() {
        service.delete(product);
        verify(productRepository).delete(product);
    }

    /* ---------- updateProduct ---------- */

    @Nested
    class UpdateProduct {

        @Test
        void updatesAndSaves_whenNoStockMovement() {
            ProductRecordDto dto = new ProductRecordDto("ABC", ProductType.ELETRONIC, BigDecimal.valueOf(6), 20);

            when(stockMovementRepository.existsByProductId(productId)).thenReturn(false);
            when(productRepository.save(product)).thenReturn(product);

            ProductModel updated = service.updateProduct(dto, product);

            assertEquals(BigDecimal.valueOf(6), updated.getSupplierPrice());
            assertEquals(20, updated.getStockQuantity());
            verify(productRepository).save(product);
        }

        @Test
        void throwsException_whenStockMovementExists() {
            when(stockMovementRepository.existsByProductId(productId)).thenReturn(true);

            ProductRecordDto dto = new ProductRecordDto("ABC", ProductType.ELETRONIC, BigDecimal.TEN, 1);

            assertThrows(ProductMovementStockExist.class, () -> service.updateProduct(dto, product));
            verify(productRepository, never()).save(any());
        }
    }

    /* ---------- findByCode ---------- */

    @Nested
    class FindByCode {

        @Test
        void returnsProduct_whenFound() {
            when(productRepository.findByCode("ABC")).thenReturn(Optional.of(product));
            assertEquals(product, service.findByCode("ABC").orElseThrow());
        }

        @Test
        void throwsNotFound_whenMissing() {
            when(productRepository.findByCode("ZZZ")).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> service.findByCode("ZZZ"));
        }
    }

    /* ---------- getProfitProduct ---------- */

    @Test
    void getProfitProduct_delegatesToRepository() {
        ProductProfitResponseDto dto = new ProductProfitResponseDto(productId, "100L", 12l, BigDecimal.valueOf(100));
        when(productRepository.findProductProfits(productId)).thenReturn(dto);

        assertEquals(dto, service.getProfitProduct(productId));
        verify(productRepository).findProductProfits(productId);
    }

    /* ---------- getProductsWithQuantitiesByType ---------- */

    @Test
    void getProductsWithQuantitiesByType_delegatesToRepository() {
        ProductAggregateResponseDto dto = new ProductAggregateResponseDto(productId, "1021", ProductType.ELETRONIC, 100l);
        when(productRepository.findProductsWithQuantitiesByType("eletronic", productId)).thenReturn(dto);

        assertEquals(dto, service.getProductsWithQuantitiesByType("eletronic", productId));
        verify(productRepository).findProductsWithQuantitiesByType("eletronic", productId);
    }
}
