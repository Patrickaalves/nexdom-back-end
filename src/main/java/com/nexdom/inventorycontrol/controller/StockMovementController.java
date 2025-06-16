package com.nexdom.inventorycontrol.controller;

import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.service.StockMovementService;
import com.nexdom.inventorycontrol.specifications.SpecificationStockMovement;
import com.nexdom.inventorycontrol.validations.StockMovementValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/stock-movement")
public class StockMovementController {

    final StockMovementService stockMovementService;
    final StockMovementValidator stockMovementValidator;

    public StockMovementController(StockMovementService stockMovementService, StockMovementValidator stockMovementValidator) {
        this.stockMovementService = stockMovementService;
        this.stockMovementValidator = stockMovementValidator;
    }

    @PostMapping
    public ResponseEntity<Object> saveStockMovement(@RequestBody @Validated(StockMovementRecordDto.stockMovementView.stockMovementPost.class)
                                                    StockMovementRecordDto stockMovementRecordDto,
                                                    Errors errors) {
        stockMovementValidator.validate(stockMovementRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.registerStockMovement(stockMovementRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<StockMovementModel>> getAllStockMovements(SpecificationStockMovement.StockMovementSpec spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(stockMovementService.findAll(spec, pageable));
    }

    @GetMapping("/{stockMovementId}")
    public ResponseEntity<Object> getOneStockMovement(@PathVariable UUID stockMovementId) {
        return ResponseEntity.status(HttpStatus.OK).body(stockMovementService.findById(stockMovementId).get());
    }

    @DeleteMapping("/{stockMovementId}")
    public ResponseEntity<Object> deleteStockMovement(@PathVariable UUID stockMovementId) {
        stockMovementService.delete(stockMovementService.findById(stockMovementId).get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("StockMovement deleted sucessfully");
    }
}
