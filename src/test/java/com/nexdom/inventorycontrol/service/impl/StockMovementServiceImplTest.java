package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.StockMovementRecordDto;
import com.nexdom.inventorycontrol.dtos.response.StockMovementResponseDto;
import com.nexdom.inventorycontrol.enums.OperationType;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.repositories.StockMovementRepository;
import com.nexdom.inventorycontrol.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceImplTest {

    @Mock  
    StockMovementRepository stockMovementRepository;
    
    @Mock  
    ProductService productService;
    
    @Mock  
    ProductModel product;

    @InjectMocks
    StockMovementServiceImpl service;

    UUID productId;
    UUID movementId;

    @BeforeEach
    void setUp() {
        productId   = UUID.randomUUID();
        movementId  = UUID.randomUUID();
    }


    /* ---------- 1. registerStockMovement ---------- */
    @Nested
    class RegisterStockMovement {

        @Test
        // Testando uma entrada de produto
        void entry_updatesStockAndSaves() {
            int qty = 10;
            StockMovementRecordDto dto = new StockMovementRecordDto(
                    productId,
                    OperationType.ENTRY,
                    BigDecimal.valueOf(300),
                    LocalDateTime.now(),
                    qty
            );

            when(productService.findById(productId)).thenReturn(Optional.of(product));

            when(stockMovementRepository.save(any(StockMovementModel.class))).thenAnswer(a -> a.getArgument(0));

            StockMovementModel result = service.registerStockMovement(dto);

            verify(product).creditStock(qty);
            verify(stockMovementRepository).save(any(StockMovementModel.class));

            assertEquals(product, result.getProduct());      // produto amarrado
            assertEquals(OperationType.ENTRY, result.getOperationType());
            assertEquals(qty, result.getMovementQuantity());
        }

        @Test
        // Testando uma saida de produto
        void exit_updatesStockAndSaves() {
            int qty = 10;
            BigDecimal unitPrice = BigDecimal.valueOf(3);

            StockMovementRecordDto dto = new StockMovementRecordDto(
                    productId,
                    OperationType.EXIT,
                    unitPrice,
                    LocalDateTime.now(),
                    qty
            );

            when(productService.findById(productId)).thenReturn(Optional.of(product));
            when(stockMovementRepository.save(any(StockMovementModel.class))).thenAnswer(a -> a.getArgument(0));

            StockMovementModel result = service.registerStockMovement(dto);

            verify(product).debitStock(qty);

            verify(stockMovementRepository).save(any(StockMovementModel.class));

            assertEquals(OperationType.EXIT, result.getOperationType());
            assertEquals(qty, result.getMovementQuantity());
        }
    }

    /* ---------- 2. findById / findByIdDto ---------- */
    @Nested
    class FindById {

        @Test
        void returnsMovement_whenExists() {
            StockMovementModel mov = new StockMovementModel();
            when(stockMovementRepository.findById(movementId)).thenReturn(Optional.of(mov));

            StockMovementModel result = service.findById(movementId).orElseThrow();

            assertSame(mov, result);
        }

        @Test
        void throwsNotFound_whenMissing() {
            when(stockMovementRepository.findById(movementId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.findById(movementId));
        }

        @Test
        void findByIdDto_wrapsEntityInDto() {
            StockMovementModel mov = new StockMovementModel();
            mov.setMovementQuantity(7);
            when(stockMovementRepository.findById(movementId)).thenReturn(Optional.of(mov));

            StockMovementResponseDto dto = service.findByIdDto(movementId);

            assertEquals(7, dto.movementQuantity());
        }
    }

    /* ---------- 3. delete ---------- */
    @Nested
    class DeleteMovement {

        @Test
        void exitMovement_revertsStockAndDeletes() {
            StockMovementModel mov = new StockMovementModel();
            mov.setOperationType(OperationType.EXIT);
            mov.setMovementQuantity(4);
            mov.setProduct(product);

            service.delete(mov);

            verify(product).creditStock(4);
            verify(stockMovementRepository).delete(mov);
        }

        @Test
        void entryMovement_revertsStockAndDeletes() {
            StockMovementModel mov = new StockMovementModel();
            mov.setOperationType(OperationType.ENTRY);
            mov.setMovementQuantity(8);
            mov.setProduct(product);

            service.delete(mov);

            verify(product).debitStock(8);
            verify(stockMovementRepository).delete(mov);
        }
    }
}
