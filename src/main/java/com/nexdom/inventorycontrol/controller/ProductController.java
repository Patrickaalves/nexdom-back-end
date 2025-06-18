package com.nexdom.inventorycontrol.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.dtos.response.ProductResponseDto;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.service.ProductService;
import com.nexdom.inventorycontrol.specifications.SpecificationProduct;
import com.nexdom.inventorycontrol.validations.ProductValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody @Validated(ProductRecordDto.ProductView.ProductPost.class)
                                              @JsonView(ProductRecordDto.ProductView.ProductPost.class) ProductRecordDto productDto,
                                              Errors errors) {
        productValidator.validate(productDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto(productService.registerProduct(productDto)));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(SpecificationProduct.ProductSpec spec,
                                                             Pageable pageable) {
        Page<ProductResponseDto> dtoPage = productService
                .findAll(spec, pageable)
                .map(ProductResponseDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getOneProduct(@PathVariable UUID productId) {
        Optional<ProductModel> optionalProductModel = productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(optionalProductModel.get());
    }

    @GetMapping("/profit-product/{productId}")
    public ResponseEntity<Object> getProfitProduct(@PathVariable UUID productId) {
        productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProfitProduct(productId));
    }

    // Refatorar esse metodo
    @GetMapping("/find-products-type/{type}/{productId}")
    public ResponseEntity<Object> getProductsWithQuantitiesByType(@PathVariable String type,
                                                                  @PathVariable UUID productId) {
        productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsWithQuantitiesByType(type.toUpperCase(), productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID productId,
                                                @RequestBody @Validated(ProductRecordDto.ProductView.ProductPut.class)
                                                ProductRecordDto productDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productDto, productService.findById(productId).get()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID productId) {
        productService.delete(productService.findById(productId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso!");
    }
}
