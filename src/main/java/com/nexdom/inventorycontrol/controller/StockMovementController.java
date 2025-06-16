package com.nexdom.inventorycontrol.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.service.StockMovementService;
import com.nexdom.inventorycontrol.validations.StockMovementValidator;
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
                                                    @JsonView(StockMovementRecordDto.stockMovementView.stockMovementPost.class) StockMovementRecordDto stockMovementRecordDto,
                                                    Errors errors) {
        stockMovementValidator.validate(stockMovementRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.registerStockMovement(stockMovementRecordDto));
    }

    @DeleteMapping("/{stockMovementId}")
    public ResponseEntity<Object> deleteStockMovement(@PathVariable UUID stockMovementId) {
        stockMovementService.delete(stockMovementService.findById(stockMovementId).get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("StockMovement deleted sucessfully");
    }
}
