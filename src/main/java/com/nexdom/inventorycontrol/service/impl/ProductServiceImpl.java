package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.ProductModel;
import com.nexdom.inventorycontrol.repositories.ProductRepository;
import com.nexdom.inventorycontrol.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public ProductModel registerProduct(ProductRecordDto productDto) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        return productRepository.save(productModel);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public Page<ProductModel> findAll(Specification<ProductModel> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<ProductModel> findById(UUID productId) {
        Optional<ProductModel> productModelOptional = productRepository.findById(productId);
        if (productModelOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        return productModelOptional;
    }

    @Transactional
    @Override
    public void delete(ProductModel productModel) {
        productRepository.delete(productModel);
    }

    @Transactional
    @Override
    public ProductModel updateProduct(ProductRecordDto productDto, ProductModel productModel) {
        productModel.setProductType(productDto.productType());
        productModel.setSupplierPrice(productDto.supplierPrice());
        productModel.setStockQuantity(productDto.stockQuantity());
        return productRepository.save(productModel);
    }

    @Override
    public Optional<ProductModel> findByCode(String productCode) {
        Optional<ProductModel> productModelOptional = productRepository.findByCode(productCode);
        if (productModelOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        return productModelOptional;
    }
}
