package com.nexdom.inventorycontrol.repositories;

import com.nexdom.inventorycontrol.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> , JpaSpecificationExecutor<CustomerModel> {
    boolean existsByCode(String code);
}
