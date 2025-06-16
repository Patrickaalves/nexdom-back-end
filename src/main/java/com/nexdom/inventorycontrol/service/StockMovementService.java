package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.response.StockMovementRecordDto;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface StockMovementService {
    StockMovementModel registerStockMovement(StockMovementRecordDto stockMovementRecordDto);

    Optional<StockMovementModel> findById(UUID stockMovementId);

    void delete(StockMovementModel stockMovementModel);

    Page<StockMovementModel> findAll(Specification<StockMovementModel> spec, Pageable pageable);
}
