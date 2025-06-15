package com.nexdom.inventorycontrol.controller;

import com.nexdom.inventorycontrol.dtos.response.ProductDto;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.specifications.SpecificationProduct;
import com.nexdom.inventorycontrol.validations.ProductValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    final ProductService productService;
    final ProductValidator productValidator;

    public ProductController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    @GetMapping
    public ResponseEntity<Page<ProductModel>> getAllProducts(SpecificationProduct.ProductSpec spec,
                                                             Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(spec, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getOneProduct(@PathVariable UUID productId) {
        Optional<ProductModel> optionalProductModel = productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(optionalProductModel.get());
    }

    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody ProductDto productDto,
                                              Errors errors) {
        productValidator.validate(productDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.registerProduct(productDto));
    }
}
