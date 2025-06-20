package com.nexdom.inventorycontrol.service.impl;

import com.nexdom.inventorycontrol.dtos.CustomerRecordDto;
import com.nexdom.inventorycontrol.exceptions.NotFoundException;
import com.nexdom.inventorycontrol.model.CustomerModel;
import com.nexdom.inventorycontrol.repositories.CustomerRepository;
import com.nexdom.inventorycontrol.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public CustomerModel registerCustomer(CustomerRecordDto customerRecordDto) {
        CustomerModel customerModel = new CustomerModel();
        BeanUtils.copyProperties(customerRecordDto, customerModel);

        return customerRepository.save(customerModel);
    }

    @Override
    public Page<CustomerModel> findAll(Specification<CustomerModel> spec, Pageable pageable) {
        return customerRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<CustomerModel> findById(UUID customerId) {
        Optional<CustomerModel> customerModel = customerRepository.findById(customerId);
        if (customerModel.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado");
        }

        return customerModel;
    }

    @Override
    public CustomerModel findByIdModel(UUID customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Transactional
    @Override
    public CustomerModel updateCustomer(CustomerRecordDto customerRecordDto, CustomerModel customerModel) {
        customerModel.setName(customerRecordDto.name());
        customerModel.setPhone(customerRecordDto.phone());

        return customerRepository.save(customerModel);
    }

    @Transactional
    @Override
    public void delete(CustomerModel customerModel) {
        customerRepository.delete(customerModel);
    }

    @Override
    public boolean existsByCode(String code) {
        return customerRepository.existsByCode(code);
    }
}
