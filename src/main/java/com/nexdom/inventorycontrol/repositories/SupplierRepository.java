package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<SupplierModel, UUID> {
}
