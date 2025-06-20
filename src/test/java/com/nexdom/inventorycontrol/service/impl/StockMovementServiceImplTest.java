package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.StockMovementRecordDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.BusinessRuleException;
import com.nexdom.inventorycontrol.model.*;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import com.nexdom.inventorycontrol.service.CustomerService;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockMovementServiceImplTest {

    @InjectMocks
    private StockMovementServiceImpl service;

    @Mock
    private StockMovementRepository repository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private SupplierService supplierService;

    private ProductModel product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new ProductModel();
        product.setStockQuantity(10);
        product.setCode("P001");
        product.setSupplierPrice(BigDecimal.valueOf(100));
    }

    private StockMovementRecordDto createDto(OperationType type, UUID supplierId, UUID customerId) {
        return new StockMovementRecordDto(
                UUID.randomUUID(),                  // productId
                type,                               // operationType
                BigDecimal.valueOf(10.0),           // salePrice
                LocalDateTime.now(),                // saleDate
                customerId,                         // customerId
                supplierId,                         // supplierId
                5                                   // movementQuantity
        );
    }

    @Test
    void testRegisterStockMovement_entryWithSupplier_shouldSucceed() {
        UUID supplierId = UUID.randomUUID();
        StockMovementRecordDto dto = createDto(OperationType.ENTRY, supplierId, null);

        when(productService.findById(dto.productId())).thenReturn(Optional.of(product));
        when(supplierService.findByIdModel(supplierId)).thenReturn(new SupplierModel());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var saved = service.registerStockMovement(dto);

        assertEquals(product, saved.getProduct());
        verify(repository).save(any());
        assertEquals(15, product.getStockQuantity());
    }

    @Test
    void testRegisterStockMovement_exitWithCustomer_shouldSucceed() {
        UUID customerId = UUID.randomUUID();
        StockMovementRecordDto dto = createDto(OperationType.EXIT, null, customerId);

        when(productService.findById(dto.productId())).thenReturn(Optional.of(product));
        when(customerService.findByIdModel(customerId)).thenReturn(new CustomerModel());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var saved = service.registerStockMovement(dto);

        assertEquals(5, product.getStockQuantity());
        verify(repository).save(any());
    }

    @Test
    void testRegisterStockMovement_entryWithoutSupplier_shouldThrow() {
        StockMovementRecordDto dto = createDto(OperationType.ENTRY, null, null);
        when(productService.findById(dto.productId())).thenReturn(Optional.of(product));

        assertThrows(BusinessRuleException.class, () -> service.registerStockMovement(dto));
    }

    @Test
    void testRegisterStockMovement_exitWithoutCustomer_shouldThrow() {
        StockMovementRecordDto dto = createDto(OperationType.EXIT, null, null);
        when(productService.findById(dto.productId())).thenReturn(Optional.of(product));

        assertThrows(BusinessRuleException.class, () -> service.registerStockMovement(dto));
    }

    @Test
    void testDelete_entry_shouldDebitStock() {
        StockMovementModel model = new StockMovementModel();
        model.setProduct(product);
        model.setOperationType(OperationType.ENTRY);
        model.setMovementQuantity(5);

        SupplierModel supplier = new SupplierModel();
        supplier.setSupplierId(UUID.randomUUID());
        model.setSupplier(supplier);

        service.delete(model);

        assertEquals(5, product.getStockQuantity());
        verify(repository).delete(model);
    }

    @Test
    void testDelete_exit_shouldCreditStock() {
        product.setStockQuantity(5);

        StockMovementModel model = new StockMovementModel();
        model.setProduct(product);
        model.setOperationType(OperationType.EXIT);
        model.setMovementQuantity(5);

        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(UUID.randomUUID());
        model.setCustomer(customer);

        service.delete(model);

        assertEquals(10, product.getStockQuantity());
        verify(repository).delete(model);
    }

    @Test
    void testDelete_entryWithoutSupplier_shouldThrow() {
        StockMovementModel model = new StockMovementModel();
        model.setOperationType(OperationType.ENTRY);
        model.setProduct(product);
        model.setMovementQuantity(5);

        assertThrows(BusinessRuleException.class, () -> service.delete(model));
    }

    @Test
    void testDelete_exitWithoutCustomer_shouldThrow() {
        StockMovementModel model = new StockMovementModel();
        model.setOperationType(OperationType.EXIT);
        model.setProduct(product);
        model.setMovementQuantity(5);

        assertThrows(BusinessRuleException.class, () -> service.delete(model));
    }
}
