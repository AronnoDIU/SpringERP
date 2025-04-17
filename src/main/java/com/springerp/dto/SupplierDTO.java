package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SupplierInteractionDTO> interactions;
}
