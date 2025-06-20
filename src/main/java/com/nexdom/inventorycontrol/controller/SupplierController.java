package com.nexdom.inventorycontrol.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.dtos.response.SupplierResponseDto;
import com.nexdom.inventorycontrol.service.SupplierService;
import com.nexdom.inventorycontrol.specifications.SpecificationSupplier;
import com.nexdom.inventorycontrol.validations.SupplierValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<Object> saveSupplier(@RequestBody @Validated({SupplierRecordDto.SupplierView.SupplierPost.class})
                                         @JsonView(SupplierRecordDto.SupplierView.SupplierPost.class) SupplierRecordDto supplierRecordDto,
                                         Errors errors) {
        supplierValidator.validate(supplierRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.registerSupplier(supplierRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDto>> getAllProducts(SpecificationSupplier.SupplierSpec spec,
                                                                    Pageable pageable) {
        Page<SupplierResponseDto> dtoProducts = supplierService
                .findAll(spec, pageable)
                .map(SupplierResponseDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(dtoProducts);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<Object> getOneSupplier(@PathVariable UUID supplierId) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.findById(supplierId).get());
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<Object> updateSupplier(@PathVariable UUID supplierId,
                                                 @RequestBody @Validated(SupplierRecordDto.SupplierView.SupplierPut.class)
                                                 @JsonView(SupplierRecordDto.SupplierView.SupplierPut.class) SupplierRecordDto supplierRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new SupplierResponseDto(supplierService.updateSupplier(supplierRecordDto,supplierService.findById(supplierId).get())));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID supplierId) {
        supplierService.delete(supplierService.findById(supplierId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Fornecedor deletado com sucesso!");
    }

}
