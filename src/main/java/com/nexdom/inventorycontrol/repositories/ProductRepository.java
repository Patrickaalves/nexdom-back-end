package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    boolean existsByCode(String code);
}
