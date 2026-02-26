package com.springerp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User entity representing an application user.
 * Audit fields (createdAt, updatedAt, isDeleted, etc.) are inherited from BaseEntity.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Date of birth cannot be null")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Role cannot be blank")
    @Column(nullable = false, length = 50)
    private String role;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, message = "Address must be at least 5 characters")
    @Column(length = 255)
    private String address;

    @Column(name = "reset_password_token", length = 100)
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expiry")
    private LocalDateTime resetPasswordTokenExpiry;
}
