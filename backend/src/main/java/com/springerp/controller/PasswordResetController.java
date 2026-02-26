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

/**
 * Controller for password reset flow.
 * Note: context-path /api/v1 is already configured in application properties,
 * so this controller is accessible at /api/v1/auth/forgot-password and /api/v1/auth/reset-password.
 */
@RestController
@RequestMapping("/auth")
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
        } catch (Exception e) {
            log.error("Error processing forgot password request", e);
        }
        // Always return success to avoid exposing whether email exists (security best practice)
        return ResponseEntity.ok(new ApiResponse(true,
            "If the email exists in our system, you will receive a password reset link"));
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
                .body(new ApiResponse(false, "Error resetting password. Please try again."));
        }
    }
}
