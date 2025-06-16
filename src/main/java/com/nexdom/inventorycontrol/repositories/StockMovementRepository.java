package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.StockMovementModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovementModel, UUID > {
}
