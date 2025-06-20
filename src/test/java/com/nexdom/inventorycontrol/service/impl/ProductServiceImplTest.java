package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.enums.ProductType;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.exceptions.ProductMovementStockExist;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.SupplierModel;
import com.nexdom.inventorycontrol.repositories.ProductRepository;
import com.nexdom.inventorycontrol.service.StockMovementService;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    StockMovementService stockMovementService;

    @Mock
    SupplierService supplierService;

    @InjectMocks
    ProductServiceImpl productService;

    UUID supplierId;
    UUID productId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        supplierId = UUID.randomUUID();
        productId = UUID.randomUUID();
    }

    @Test
    void registerProduct_success() {
        ProductRecordDto dto = new ProductRecordDto(
                "CODE123",
                ProductType.ELETRONIC,  // substitua por um valor real do seu enum
                supplierId,
                new BigDecimal("100.00"),
                10
        );

        SupplierModel supplier = new SupplierModel();
        supplier.setSupplierId(supplierId);

        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(productRepository.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.registerProduct(dto);

        assertNotNull(result);
        assertEquals(dto.code(), result.getCode());
        assertEquals(dto.supplierPrice(), result.getSupplierPrice());
        assertEquals(dto.stockQuantity(), result.getStockQuantity());
        assertEquals(supplier, result.getSupplier());

        verify(supplierService).findById(supplierId);
        verify(productRepository).save(any(ProductModel.class));
    }

    @Test
    void registerProduct_supplierNotFound_throws() {
        ProductRecordDto dto = new ProductRecordDto(
                "CODE123",
                ProductType.ELETRONIC,
                supplierId,
                new BigDecimal("100.00"),
                10
        );

        when(supplierService.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.registerProduct(dto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_withNoStockMovements_success() {
        ProductRecordDto dto = new ProductRecordDto(
                "NEWCODE",
                ProductType.ELETRONIC,
                supplierId,
                new BigDecimal("150.00"),
                20
        );

        ProductModel existingProduct = new ProductModel();
        existingProduct.setProductId(productId);

        when(stockMovementService.existsByProductId(productId)).thenReturn(false);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        ProductModel updated = productService.updateProduct(dto, existingProduct);

        assertEquals(dto.code(), updated.getCode());
        assertEquals(dto.productType(), updated.getProductType());
        assertEquals(dto.supplierPrice(), updated.getSupplierPrice());
        assertEquals(dto.stockQuantity(), updated.getStockQuantity());
    }

    @Test
    void updateProduct_withStockMovements_throws() {
        ProductRecordDto dto = new ProductRecordDto(
                "NEWCODE",
                ProductType.ELETRONIC,
                supplierId,
                new BigDecimal("150.00"),
                20
        );

        ProductModel existingProduct = new ProductModel();
        existingProduct.setProductId(productId);

        when(stockMovementService.existsByProductId(productId)).thenReturn(true);

        assertThrows(ProductMovementStockExist.class, () -> productService.updateProduct(dto, existingProduct));
        verify(productRepository, never()).save(any());
    }

    @Test
    void findById_existingProduct_success() {
        ProductModel product = new ProductModel();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<ProductModel> found = productService.findById(productId);

        assertTrue(found.isPresent());
        assertEquals(productId, found.get().getProductId());
    }

    @Test
    void findById_notFound_throws() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findById(productId));
    }

    @Test
    void findByCode_existingProduct_success() {
        ProductModel product = new ProductModel();
        product.setCode("CODE123");

        when(productRepository.findByCode("CODE123")).thenReturn(Optional.of(product));

        Optional<ProductModel> found = productService.findByCode("CODE123");

        assertTrue(found.isPresent());
        assertEquals("CODE123", found.get().getCode());
    }

    @Test
    void findByCode_notFound_throws() {
        when(productRepository.findByCode("CODE123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findByCode("CODE123"));
    }

}
