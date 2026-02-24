package com.springerp.service;

import com.springerp.entity.Company;
import com.springerp.entity.Customer;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setCompanyName("Test Corp");

        customer = Customer.builder()
                .name("Alice Smith")
                .email("alice@example.com")
                .phone("+1-555-0101")
                .address("10 Elm Street")
                .company(company)
                .build();
        customer.setId(1L);
        customer.setIsDeleted(false);
    }

    @Test
    void createCustomer_savesAndReturnsCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertThat(result).isEqualTo(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_updatesAllEditableFields() {
        Customer update = Customer.builder()
                .name("Alice Johnson")
                .email("alicej@example.com")
                .phone("+1-555-0202")
                .address("20 Oak Avenue")
                .company(company)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.updateCustomer(1L, update);

        assertThat(result.getName()).isEqualTo("Alice Johnson");
        assertThat(result.getEmail()).isEqualTo("alicej@example.com");
        assertThat(result.getPhone()).isEqualTo("+1-555-0202");
        assertThat(result.getAddress()).isEqualTo("20 Oak Avenue");
    }

    @Test
    void updateCustomer_withNonExistentId_throwsResourceNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(99L, customer))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateCustomer_withSoftDeletedCustomer_throwsResourceNotFoundException() {
        customer.setIsDeleted(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.updateCustomer(1L, customer))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCustomer_performsSoftDelete() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        customerService.deleteCustomer(1L);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        assertThat(captor.getValue().getIsDeleted()).isTrue();
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void deleteCustomer_withNonExistentId_throwsResourceNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomer(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteCustomer_withAlreadySoftDeletedCustomer_throwsResourceNotFoundException() {
        customer.setIsDeleted(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.deleteCustomer(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCustomer_returnsActiveCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomer(1L);

        assertThat(result).isEqualTo(customer);
    }

    @Test
    void getCustomer_withNonExistentId_throwsResourceNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomer(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getCustomer_withSoftDeletedCustomer_throwsResourceNotFoundException() {
        customer.setIsDeleted(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.getCustomer(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllCustomers_returnsOnlyNonDeletedCustomers() {
        when(customerRepository.findByIsDeletedFalse()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertThat(result).containsExactly(customer);
        verify(customerRepository).findByIsDeletedFalse();
    }

    @Test
    void getAllCustomers_returnsEmptyListWhenNoneExist() {
        when(customerRepository.findByIsDeletedFalse()).thenReturn(List.of());

        List<Customer> result = customerService.getAllCustomers();

        assertThat(result).isEmpty();
    }

    @Test
    void searchCustomers_delegatesToRepositoryWithQueryTerm() {
        when(customerRepository.findByNameContainingIgnoreCase("alice")).thenReturn(List.of(customer));

        List<Customer> result = customerService.searchCustomers("alice");

        assertThat(result).containsExactly(customer);
        verify(customerRepository).findByNameContainingIgnoreCase("alice");
    }

    @Test
    void searchCustomers_returnsEmptyListWhenNoMatchFound() {
        when(customerRepository.findByNameContainingIgnoreCase("unknown")).thenReturn(List.of());

        List<Customer> result = customerService.searchCustomers("unknown");

        assertThat(result).isEmpty();
    }
}

