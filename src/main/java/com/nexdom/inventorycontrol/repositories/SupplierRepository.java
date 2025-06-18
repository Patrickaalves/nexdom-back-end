package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<SupplierModel, UUID> , JpaSpecificationExecutor<SupplierModel> {
    Boolean existsByCode(String code);
}
