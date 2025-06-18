package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.response.ProductAggregateResponseDto;
import com.nexdom.inventorycontrol.dtos.response.ProductProfitResponseDto;
import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.model.ProductModel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductModel registerProduct(@Valid ProductRecordDto productDto);

    boolean existsByCode(String code);

    Page<ProductModel> findAll(Specification<ProductModel> spec, Pageable pageable);

    Optional<ProductModel> findById(UUID productId);

    void delete(ProductModel productModel);

    ProductModel updateProduct(ProductRecordDto productDto, ProductModel productModel);

    Optional<ProductModel> findByCode(String productCode);

    ProductProfitResponseDto getProfitProduct(UUID productId);

    ProductAggregateResponseDto getProductsWithQuantitiesByType(String type, UUID productId);
}
