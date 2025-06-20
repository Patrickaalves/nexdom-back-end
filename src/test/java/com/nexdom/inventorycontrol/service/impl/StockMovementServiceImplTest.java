package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.StockMovementRecordDto;
import com.nexdom.inventorycontrol.dtos.response.StockMovementResponseDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.BusinessRuleException;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
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

    @Mock
    StockMovementRepository movementRepo;
    @Mock
    CustomerService customerSvc;
    @Mock
    ProductService productSvc;
    @Mock
    SupplierService supplierSvc;

    @InjectMocks
    StockMovementServiceImpl service;

    UUID productId;
    UUID supplierId;
    UUID customerId;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        supplierId = UUID.randomUUID();
        customerId = UUID.randomUUID();
    }

    // ---------- register ----------------------------------------------------

    @Test
    void entry_with_supplier_ok() {
        StockMovementRecordDto dto = new StockMovementRecordDto(
                productId,
                OperationType.ENTRY,
                new BigDecimal("40"),
                new BigDecimal("20"),
                LocalDateTime.now(),
                null,
                supplierId,
                5
        );

        ProductModel product = new ProductModel();
        product.setProductId(productId);
        product.setStockQuantity(1);

        SupplierModel supplier = new SupplierModel();
        supplier.setSupplierId(supplierId);

        when(productSvc.findById(productId)).thenReturn(Optional.of(product));
        when(supplierSvc.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(movementRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        StockMovementModel res = service.registerStockMovement(dto);

        assertEquals(6, product.getStockQuantity()); // 1 + 5
        assertEquals(OperationType.ENTRY, res.getOperationType());
    }

    @Test
    void entry_without_supplier_throw() {
        StockMovementRecordDto dto = new StockMovementRecordDto(
                productId,
                OperationType.ENTRY,
                new BigDecimal("40"),
                new BigDecimal("20"),
                LocalDateTime.now(),
                null,
                null,
                3
        );
        when(productSvc.findById(productId)).thenReturn(Optional.of(new ProductModel()));
        assertThrows(BusinessRuleException.class, () -> service.registerStockMovement(dto));
    }

    @Test
    void exit_with_customer_ok() {
        StockMovementRecordDto dto = new StockMovementRecordDto(
                productId,
                OperationType.EXIT,
                new BigDecimal("90"),
                new BigDecimal("55"),
                LocalDateTime.now(),
                customerId,
                null,
                2
        );

        ProductModel product = new ProductModel();
        product.setProductId(productId);
        product.setStockQuantity(4);
        product.setSupplierPrice(new BigDecimal("55"));

        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(customerId);

        when(productSvc.findById(productId)).thenReturn(Optional.of(product));
        when(customerSvc.findById(customerId)).thenReturn(Optional.of(customer));
        when(movementRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        StockMovementModel res = service.registerStockMovement(dto);

        assertEquals(2, product.getStockQuantity()); // 4 - 2
        assertEquals(new BigDecimal("55"), res.getCostPrice());
    }

    @Test
    void exit_without_customer_throw() {
        StockMovementRecordDto dto = new StockMovementRecordDto(
                productId,
                OperationType.EXIT,
                new BigDecimal("90"),
                new BigDecimal("55"),
                LocalDateTime.now(),
                null,
                null,
                1
        );
        when(productSvc.findById(productId)).thenReturn(Optional.of(new ProductModel()));
        assertThrows(BusinessRuleException.class, () -> service.registerStockMovement(dto));
    }

    // ---------- findById ----------------------------------------------------

    @Test
    void find_not_found_throw() {
        UUID id = UUID.randomUUID();
        when(movementRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findById(id));
    }

    @Test
    void find_dto_ok() {
        UUID id = UUID.randomUUID();
        StockMovementModel mov = new StockMovementModel();
        mov.setStockMovementId(id);
        when(movementRepo.findById(id)).thenReturn(Optional.of(mov));
        StockMovementResponseDto dto = service.findByIdDto(id);
        assertEquals(id, dto.stockMovementId());
    }

    // ---------- delete ------------------------------------------------------

    @Test
    void delete_entry_ok() {
        ProductModel product = new ProductModel();
        product.setStockQuantity(10);
        StockMovementModel mov = new StockMovementModel();
        mov.setProduct(product);
        mov.setMovementQuantity(4);
        mov.setOperationType(OperationType.ENTRY);

        service.delete(mov);

        assertEquals(6, product.getStockQuantity()); // 10 - 4
        verify(movementRepo).delete(mov);
    }

    @Test
    void delete_exit_ok() {
        ProductModel product = new ProductModel();
        product.setStockQuantity(3);
        StockMovementModel mov = new StockMovementModel();
        mov.setProduct(product);
        mov.setMovementQuantity(2);
        mov.setOperationType(OperationType.EXIT);

        service.delete(mov);

        assertEquals(5, product.getStockQuantity()); // 3 + 2
        verify(movementRepo).delete(mov);
    }

    @Test
    void delete_null_npe() {
        assertThrows(NullPointerException.class, () -> service.delete(null));
    }
}
