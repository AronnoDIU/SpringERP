package com.springerp.controller;

import com.springerp.dto.LoginRequest;
import com.springerp.entity.ErrorObject;
import com.springerp.entity.JwtResponse;
import com.springerp.entity.User;
import com.springerp.service.UserService;
import com.springerp.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        if (user.getPassword() == null) {
            return ResponseEntity.status(400).body(null);
        }
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Attempting to authenticate user: {}", loginRequest.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            
            logger.info("User authenticated successfully: {}", loginRequest.getEmail());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get UserDetails from the authentication object
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("UserDetails loaded: {}", userDetails.getUsername());
            
            // Generate JWT token using the username
            String token = jwtTokenUtil.generateToken(userDetails.getUsername());
            logger.info("JWT token generated successfully");
            
            return ResponseEntity.ok()
                    .body(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorObject(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password", new Date()));
        } catch (Exception e) {
            logger.error("Error during authentication for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                            "An error occurred during authentication: " + e.getMessage(), new Date()));
        }
    }
}
