package com.springerp.service;

import com.springerp.entity.User;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.exception.UserAlreadyExistsException;
import com.springerp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        updateFieldIfNotNull(user.getFirstName(), loggedInUser::setFirstName);
        updateFieldIfNotNull(user.getLastName(), loggedInUser::setLastName);
        updateFieldIfNotNull(user.getEmail(), loggedInUser::setEmail);
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            log.debug("Encoding updated password for user: {}", loggedInUser.getEmail());
            loggedInUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        updateFieldIfNotNull(user.getDateOfBirth(), loggedInUser::setDateOfBirth);
        updateFieldIfNotNull(user.getRole(), loggedInUser::setRole);
        updateFieldIfNotNull(user.getPhoneNumber(), loggedInUser::setPhoneNumber);
        updateFieldIfNotNull(user.getAddress(), loggedInUser::setAddress);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private <T> void updateFieldIfNotNull(T newValue, java.util.function.Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}
