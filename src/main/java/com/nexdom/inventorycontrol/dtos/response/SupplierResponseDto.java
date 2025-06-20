package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.model.SupplierModel;

import java.util.UUID;

public record SupplierResponseDto(UUID supplierId,
                                  String code,
                                  String name,
                                  String cnpj,
                                  String phone) {
    public SupplierResponseDto(SupplierModel entity) {
        this(
                entity.getSupplierId(),
                entity.getCode(),
                entity.getName(),
                entity.getCnpj(),
                entity.getPhone()
        );
    }
}
