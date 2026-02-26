package com.springerp.service;

import com.springerp.exception.ResourceNotFoundException;
import com.springerp.entity.Customer;
import com.springerp.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Creating new customer: {}", customer.getName());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
            .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        customer.setAddress(customerDetails.getAddress());
        customer.setCompany(customerDetails.getCompany());

        log.info("Updating customer with id: {}", id);
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.softDelete();
        customerRepository.save(customer);
        log.info("Soft-deleted customer with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
            .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findByIsDeletedFalse();
    }

    @Transactional(readOnly = true)
    public List<Customer> searchCustomers(String query) {
        return customerRepository.findByNameContainingIgnoreCase(query);
    }
}
