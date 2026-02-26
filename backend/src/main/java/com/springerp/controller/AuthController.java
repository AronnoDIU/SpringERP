package com.springerp.controller;

import com.springerp.dto.LoginRequest;
import com.springerp.dto.JwtResponse;
import com.springerp.entity.User;
import com.springerp.exception.ErrorResponse;
import com.springerp.service.UserService;
import com.springerp.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Registers a new user. The raw User entity is used here for simplicity;
     * the password is encoded in UserServiceImpl before persistence.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        // Don't return the password in the response
        createdUser.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Attempting to authenticate user: {}", loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails.getUsername());

            log.info("User authenticated successfully: {}", loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password"));
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An error occurred during authentication. Please try again later."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtTokenUtil.invalidateToken(token);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Token has expired"));
            }
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String newToken = jwtTokenUtil.generateToken(username);
            return ResponseEntity.ok(new JwtResponse(newToken));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid token"));
    }
}
