package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.response.ProductDto;
import com.nexdom.inventorycontrol.model.ProductModel;
import jakarta.validation.Valid;

public interface ProductService {
    ProductModel registerProduct(@Valid ProductDto productDto);
}
