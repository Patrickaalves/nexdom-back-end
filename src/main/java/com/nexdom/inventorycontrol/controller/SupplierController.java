package com.nexdom.inventorycontrol.controller;

import com.fasterxml.jackson.annotation.JsonView;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.service.SupplierService;
import com.nexdom.inventorycontrol.validations.SupplierValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    final SupplierService supplierService;
    final SupplierValidator supplierValidator;

    public SupplierController(SupplierService supplierService, SupplierValidator supplierValidator) {
        this.supplierService = supplierService;
        this.supplierValidator = supplierValidator;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated({SupplierRecordDto.SupplierView.SupplierPost.class})
                                         @JsonView(SupplierRecordDto.SupplierView.SupplierPost.class) SupplierRecordDto supplierRecordDto,
                                         Errors errors) {
        supplierValidator.validate(supplierRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.registerSupplier(supplierRecordDto));
    }
}
