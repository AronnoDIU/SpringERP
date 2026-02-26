package com.springerp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer entity. Audit fields (createdAt, updatedAt, isDeleted) are inherited from BaseEntity.
 * The company FK is mandatory for multi-tenancy enforcement.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @NotBlank(message = "Customer name cannot be blank")
    @Column(nullable = false, length = 150)
    private String name;

    @Email(message = "Email should be valid")
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @NotNull(message = "Company is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerInteraction> interactions = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();
}
