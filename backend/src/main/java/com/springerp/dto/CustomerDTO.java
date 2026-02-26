package com.springerp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank(message = "Customer name cannot be blank")
    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Size(max = 100)
    private String email;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Size(max = 255)
    private String address;

    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
