package com.nexdom.inventorycontrol.controller;

import com.nexdom.inventorycontrol.dtos.CustomerRecordDto;
import com.nexdom.inventorycontrol.dtos.response.CustomerResponseDto;
import com.nexdom.inventorycontrol.service.CustomerService;
import com.nexdom.inventorycontrol.specifications.SpecificationCustomer;
import com.nexdom.inventorycontrol.validations.CustomerValidation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    final CustomerService customerService;
    final CustomerValidation customerValidation;

    public CustomerController(CustomerService customerService, CustomerValidation customerValidation) {
        this.customerService = customerService;
        this.customerValidation = customerValidation;
    }

    @PostMapping
    public ResponseEntity<Object> saveCustomer(@RequestBody @Valid CustomerRecordDto customerRecordDto,
                                               Errors errors) {
        customerValidation.validate(customerRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customerRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(SpecificationCustomer.CustomerSpec spec,
                                                                     Pageable pageable) {

        Page<CustomerResponseDto> dtoCustomer = customerService
                .findAll(spec, pageable)
                .map(CustomerResponseDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(dtoCustomer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getOneCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(customerId).get());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable UUID customerId,
                                                 @RequestBody @Valid CustomerRecordDto customerRecordDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomerResponseDto(customerService.updateCustomer(customerRecordDto, customerService.findById(customerId).get())));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable UUID customerId) {
        customerService.delete(customerService.findById(customerId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso!");
    }
}
