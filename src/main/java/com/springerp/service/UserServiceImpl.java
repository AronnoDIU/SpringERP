package com.springerp.service;

import com.springerp.entity.User;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.exception.UserAlreadyExistsException;
import com.springerp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser() {
        return getLoggedInUser();
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        
        logger.debug("Encoding password for user: {}", user.getEmail());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        logger.debug("Password encoded successfully");
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User loggedInUser = getLoggedInUser();

        updateFieldIfNotNull(user.getFirstName(), loggedInUser::setFirstName);
        updateFieldIfNotNull(user.getLastName(), loggedInUser::setLastName);
        updateFieldIfNotNull(user.getEmail(), loggedInUser::setEmail);
        if (user.getPassword() != null) {
            logger.debug("Encoding updated password for user: {}", loggedInUser.getEmail());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            loggedInUser.setPassword(encodedPassword);
            logger.debug("Updated password encoded successfully");
        }
        updateFieldIfNotNull(user.getDateOfBirth(), loggedInUser::setDateOfBirth);
        updateFieldIfNotNull(user.getRole(), loggedInUser::setRole);
        updateFieldIfNotNull(user.getPhoneNumber(), loggedInUser::setPhoneNumber);
        updateFieldIfNotNull(user.getAddress(), loggedInUser::setAddress);

        return userRepository.save(loggedInUser);
    }

    @Override
    public void deleteUser() {
        User user = getLoggedInUser();
        userRepository.delete(user);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
