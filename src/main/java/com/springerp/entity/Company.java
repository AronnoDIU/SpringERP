package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String registrationNumber;
    
    private String vatNumber;
    private String address;
    private String phone;
    private String email;
    private String website;
    
    @Column(nullable = false)
    private String currency;
    
    private String logoUrl;
    
    @OneToMany(mappedBy = "company")
    private List<Invoice> invoices;
    
    @OneToMany(mappedBy = "company")
    private List<Customer> customers;
    
    @OneToMany(mappedBy = "company")
    private List<Order> orders;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
