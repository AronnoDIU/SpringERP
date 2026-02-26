package com.springerp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 2, message = "Product name must be at least 2 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 5, message = "Description must be at least 5 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Stock cannot be null")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
