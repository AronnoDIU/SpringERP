package com.springerp.service;

import com.springerp.dto.ForgotPasswordRequest;
import com.springerp.dto.ResetPasswordRequest;
import com.springerp.entity.User;
import com.springerp.exception.InvalidTokenException;
import com.springerp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final String frontendUrl;
    private final long tokenExpirationMs;

    public PasswordResetService(
            UserRepository userRepository,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder,
            @Value("${app.frontend.url}") String frontendUrl,
            @Value("${app.password-reset.token.expiration:3600000}") long tokenExpirationMs) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.frontendUrl = frontendUrl;
        this.tokenExpirationMs = tokenExpirationMs;
    }

    @Transactional
    public void processForgotPasswordRequest(ForgotPasswordRequest request) {
        // Find user but don't expose whether they exist
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            String token = generateResetToken();
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusSeconds(tokenExpirationMs / 1000));
            userRepository.save(user);
            sendResetPasswordEmail(user.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid password reset token"));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Password reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendResetPasswordEmail(String email, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(buildEmailContent(token), true);

            mailSender.send(message);
            log.info("Password reset email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String buildEmailContent(String token) {
        return String.format("""
            <html>
            <body>
                <h2>Password Reset Request</h2>
                <p>To reset your password, click the link below:</p>
                <p><a href="%s/reset-password?token=%s">Reset Password</a></p>
                <p>This link will expire in %d minutes.</p>
                <p>If you didn't request a password reset, please ignore this email.</p>
            </body>
            </html>
            """,
            frontendUrl,
            token,
            tokenExpirationMs / (60 * 1000));
    }
}
