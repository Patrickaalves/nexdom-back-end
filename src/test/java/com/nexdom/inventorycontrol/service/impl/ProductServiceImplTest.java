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

    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    private ProductRepository repository;

    @Mock
    private StockMovementService stockMovementService;

    @Mock
    private SupplierService supplierService;

    private SupplierModel supplier;
    private ProductRecordDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        supplier = new SupplierModel();
        supplier.setSupplierId(UUID.randomUUID());

        dto = new ProductRecordDto(
                "PROD001",              // code
                ProductType.ELETRONIC,       // productType
                supplier.getSupplierId(),    // supplier (UUID)
                BigDecimal.valueOf(50.0),    // supplierPrice
                10                           // stockQuantity
        );
    }

    @Test
    void testRegisterProduct_shouldSaveProductWithSupplier() {
        when(supplierService.findById(dto.supplier())).thenReturn(Optional.of(supplier));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductModel saved = service.registerProduct(dto);

        assertEquals(dto.code(), saved.getCode());
        assertEquals(dto.supplierPrice(), saved.getSupplierPrice());
        assertEquals(supplier, saved.getSupplier());
    }

    @Test
    void testExistsByCode_shouldReturnTrue() {
        when(repository.existsByCode("PROD001")).thenReturn(true);
        assertTrue(service.existsByCode("PROD001"));
    }

    @Test
    void testFindById_shouldReturnProduct() {
        UUID id = UUID.randomUUID();
        ProductModel product = new ProductModel();
        product.setProductId(id);
        when(repository.findById(id)).thenReturn(Optional.of(product));

        Optional<ProductModel> result = service.findById(id);
        assertEquals(product, result.get());
    }

    @Test
    void testFindById_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findById(id));
    }

    @Test
    void testDelete_shouldCallRepositoryDelete() {
        ProductModel product = new ProductModel();
        service.delete(product);
        verify(repository).delete(product);
    }

    @Test
    void testUpdateProduct_shouldUpdateIfNoStockMovement() {
        UUID productId = UUID.randomUUID();
        ProductModel product = new ProductModel();
        product.setProductId(productId);

        when(stockMovementService.existsByProductId(productId)).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductModel updated = service.updateProduct(dto, product);

        assertEquals(dto.stockQuantity(), updated.getStockQuantity());
        assertEquals(dto.supplierPrice(), updated.getSupplierPrice());
    }

    @Test
    void testUpdateProduct_shouldThrowIfStockMovementExists() {
        UUID productId = UUID.randomUUID();
        ProductModel product = new ProductModel();
        product.setProductId(productId);

        when(stockMovementService.existsByProductId(productId)).thenReturn(true);

        assertThrows(ProductMovementStockExist.class, () -> service.updateProduct(dto, product));
    }

    @Test
    void testFindByCode_shouldReturnProduct() {
        ProductModel product = new ProductModel();
        when(repository.findByCode("PROD001")).thenReturn(Optional.of(product));
        Optional<ProductModel> result = service.findByCode("PROD001");
        assertEquals(product, result.get());
    }

    @Test
    void testFindByCode_shouldThrowNotFoundException() {
        when(repository.findByCode("NOTFOUND")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findByCode("NOTFOUND"));
    }

    @Test
    void testGetProfitProduct_shouldCallRepository() {
        UUID productId = UUID.randomUUID();
        service.getProfitProduct(productId);
        verify(repository).findProductProfits(productId);
    }

    @Test
    void testGetProductsWithQuantitiesByType_shouldCallRepository() {
        UUID productId = UUID.randomUUID();
        service.getProductsWithQuantitiesByType(productId);
        verify(repository).findProductsWithQuantitiesByType(productId);
    }
}
