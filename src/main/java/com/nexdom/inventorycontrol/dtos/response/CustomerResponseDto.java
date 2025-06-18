package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.model.CustomerModel;

import java.util.UUID;

public record CustomerResponseDto(UUID customerId,
                                  String code,
                                  String name,
                                  String phone) {
    public CustomerResponseDto(CustomerModel entity) {
        this(
                entity.getCustomerId(),
                entity.getCode(),
                entity.getName(),
                entity.getPhone()
        );
    }
}
