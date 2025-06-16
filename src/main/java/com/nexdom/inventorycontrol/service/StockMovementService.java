package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.model.StockMovementModel;

import java.util.Optional;
import java.util.UUID;

public interface StockMovementService {
    StockMovementModel registerStockMovement(StockMovementRecordDto stockMovementRecordDto);

    Optional<StockMovementModel> findById(UUID stockMovementId);

    void delete(StockMovementModel stockMovementModel);
}
