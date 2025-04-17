package com.springerp.controller;

import com.springerp.dto.ForgotPasswordRequest;
import com.springerp.dto.ResetPasswordRequest;
import com.springerp.dto.ApiResponse;
import com.springerp.exception.InvalidTokenException;
import com.springerp.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
public class PasswordResetController {
    
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.processForgotPasswordRequest(request);
            return ResponseEntity.ok(new ApiResponse(true, 
                "If the email exists in our system, you will receive a password reset link"));
        } catch (Exception e) {
            log.error("Error processing forgot password request", e);
            // Don't expose it whether the email exists or not
            return ResponseEntity.ok(new ApiResponse(true, 
                "If the email exists in our system, you will receive a password reset link"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok(new ApiResponse(true, "Password reset successful"));
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error resetting password", e);
            return ResponseEntity.internalServerError()
                .body(new ApiResponse(false, "Error resetting password"));
        }
    }
}
