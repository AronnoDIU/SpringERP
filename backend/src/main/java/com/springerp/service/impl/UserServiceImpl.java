package com.springerp.service.impl;

import com.springerp.entity.User;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.exception.UserAlreadyExistsException;
import com.springerp.repository.UserRepository;
import com.springerp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getUser() {
        return getLoggedInUser();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        log.debug("Encoding password for new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User loggedInUser = getLoggedInUser();

        setIfNotNull(user.getFirstName(), loggedInUser::setFirstName);
        setIfNotNull(user.getLastName(), loggedInUser::setLastName);
        setIfNotNull(user.getEmail(), loggedInUser::setEmail);
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            log.debug("Encoding updated password for user: {}", loggedInUser.getEmail());
            loggedInUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        setIfNotNull(user.getDateOfBirth(), loggedInUser::setDateOfBirth);
        setIfNotNull(user.getRole(), loggedInUser::setRole);
        setIfNotNull(user.getPhoneNumber(), loggedInUser::setPhoneNumber);
        setIfNotNull(user.getAddress(), loggedInUser::setAddress);

        return userRepository.save(loggedInUser);
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user = getLoggedInUser();
        userRepository.delete(user);
        log.info("Deleted user account: {}", user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}

