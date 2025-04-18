package com.springerp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String registrationNumber;
    private String vatNumber;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String currency;
    private String logoUrl;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers = new ArrayList<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
    
    // Helper methods for managing bidirectional relationships
    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoice.setCompany(this);
    }
    
    public void removeInvoice(Invoice invoice) {
        invoices.remove(invoice);
        invoice.setCompany(null);
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
        customer.setCompany(this);
    }
    
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        customer.setCompany(null);
    }
    
    public void addOrder(Order order) {
        orders.add(order);
        order.setCompany(this);
    }
    
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setCompany(null);
    }
}
