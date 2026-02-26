package com.springerp.security;

import com.springerp.entity.User;
import com.springerp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = buildAuthorities(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    /**
     * Converts a role string (e.g. "ADMIN" or "ROLE_ADMIN") into a list of GrantedAuthority.
     * Ensures the "ROLE_" prefix required by Spring Security is always present.
     */
    private List<SimpleGrantedAuthority> buildAuthorities(String role) {
        if (role == null || role.isBlank()) {
            return List.of();
        }
        String normalizedRole = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase();
        return List.of(new SimpleGrantedAuthority(normalizedRole));
    }
}
