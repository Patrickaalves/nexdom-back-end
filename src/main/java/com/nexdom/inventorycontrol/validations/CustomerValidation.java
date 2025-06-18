package com.nexdom.inventorycontrol.validations;

import com.nexdom.inventorycontrol.dtos.CustomerRecordDto;
import com.nexdom.inventorycontrol.dtos.ProductRecordDto;
import com.nexdom.inventorycontrol.service.CustomerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerValidation implements Validator {

    private final Validator validator;
    final CustomerService customerService;

    public CustomerValidation(@Qualifier("defaultValidator")  Validator validator, CustomerService customerService) {
        this.validator = validator;
        this.customerService = customerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomerRecordDto customerDto = (CustomerRecordDto) o;
        validator.validate(customerDto, errors);
        if (!errors.hasErrors()) {
            validateCode(customerDto, errors);
        }
    }

    private void validateCode(CustomerRecordDto customerDto, Errors errors) {
        if (customerService.existsByCode(customerDto.code())) {
            errors.rejectValue("code", "CodeConflict", "Codigo ja existente");
        }
    }
}
