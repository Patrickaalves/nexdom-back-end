package com.nexdom.inventorycontrol.validations;

import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.service.StockMovementService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StockMovementValidator implements Validator {

    private final Validator validator;
    final StockMovementService stockMovementService;

    public StockMovementValidator(@Qualifier("defaultValidator") Validator validator, StockMovementService stockMovementService) {
        this.validator = validator;
        this.stockMovementService = stockMovementService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        StockMovementRecordDto stockMovementRecordDto = (StockMovementRecordDto) o;
        validator.validate(stockMovementRecordDto, errors);
    }
}
