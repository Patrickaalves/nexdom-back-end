package com.nexdom.inventorycontrol.service;

import com.nexdom.inventorycontrol.dtos.CustomerRecordDto;
import com.nexdom.inventorycontrol.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    CustomerModel registerCustomer(CustomerRecordDto customerRecordDto);

    Page<CustomerModel> findAll(Specification<CustomerModel> spec, Pageable pageable);

    Optional<CustomerModel> findById(UUID customerId);

    CustomerModel updateCustomer(CustomerRecordDto customerRecordDto, CustomerModel customerModel);

    void delete(CustomerModel customerModel);

    boolean existsByCode( String code);
}
