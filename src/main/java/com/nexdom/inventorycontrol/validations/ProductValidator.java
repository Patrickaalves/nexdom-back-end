package com.nexdom.inventorycontrol.validations;

import com.nexdom.inventorycontrol.dtos.response.ProductRecordDto;
import com.nexdom.inventorycontrol.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {

    private final Validator validator;
    final ProductService productService;

    public ProductValidator(@Qualifier("defaultValidator") Validator validator, ProductService productService) {
        this.validator = validator;
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductRecordDto productDto = (ProductRecordDto) o;
        validator.validate(productDto, errors);
        if (!errors.hasErrors()) {
            validateCode(productDto, errors);
        }
    }

    private void validateCode(ProductRecordDto productDto, Errors errors) {
        if (productService.existsByCode(productDto.code())) {
            errors.rejectValue("code", "CodeConflict", "Code is already taken");
        }
    }
}
