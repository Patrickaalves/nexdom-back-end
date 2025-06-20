package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.CustomerModel;
import com.nexdom.inventorycontrol.model.StockMovementModel;
import com.nexdom.inventorycontrol.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovementModel, UUID>, JpaSpecificationExecutor<StockMovementModel> {
    @Query("SELECT CASE WHEN COUNT(sm) > 0 THEN true ELSE false END FROM StockMovementModel sm WHERE sm.product.productId = :productId")
    boolean existsByProductId(@Param("productId") UUID productId);

    boolean existsByCustomer(CustomerModel customer);

    boolean existsBySupplier(SupplierModel supplier);
}
