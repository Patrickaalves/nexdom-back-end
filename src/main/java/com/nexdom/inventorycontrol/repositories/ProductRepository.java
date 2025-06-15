package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> , JpaSpecificationExecutor<ProductModel> {
    boolean existsByCode(String code);

    Optional<ProductModel> findByCode(String code);
}
