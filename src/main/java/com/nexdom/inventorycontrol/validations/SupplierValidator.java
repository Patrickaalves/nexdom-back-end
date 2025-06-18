package com.nexdom.inventorycontrol.validations;

import com.nexdom.inventorycontrol.dtos.SupplierRecordDto;
import com.nexdom.inventorycontrol.service.SupplierService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SupplierValidator implements Validator {

    private final Validator validator;
    final SupplierService supplierService;

    public SupplierValidator(@Qualifier("defaultValidator") Validator validator, SupplierService supplierService) {
        this.validator = validator;
        this.supplierService = supplierService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        SupplierRecordDto supplierDto = (SupplierRecordDto) o;
        validator.validate(supplierDto, errors);
        if (!errors.hasErrors()) {
            validateCode(supplierDto, errors);
        }
    }

    private void validateCode(SupplierRecordDto supplierDto, Errors errors) {
        if (supplierService.existsByCode(supplierDto.code())) {
            errors.rejectValue("code", "CodeConflict", "Codigo ja esta sendo usado");
        }
    }
}
