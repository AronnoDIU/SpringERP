package com.springerp.service;

import com.springerp.entity.User;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.exception.UserAlreadyExistsException;
import com.springerp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane@example.com");
        user.setPassword("rawPassword");
        user.setRole("USER");
        user.setDateOfBirth(LocalDate.of(1990, 5, 20));
        user.setPhoneNumber("+1-555-0100");
        user.setAddress("123 Main St");
    }

    @Test
    void createUser_encodesPasswordBeforeSaving() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void createUser_savesAndReturnsUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void createUser_withDuplicateEmail_throwsUserAlreadyExistsException() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("jane@example.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_updatesOnlyNonNullFields() {
        mockAuthenticatedUser("jane@example.com");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User update = new User();
        update.setFirstName("Janet");
        update.setLastName(null);

        User result = userService.updateUser(update);

        assertThat(result.getFirstName()).isEqualTo("Janet");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void updateUser_encodesPasswordWhenNewPasswordProvided() {
        mockAuthenticatedUser("jane@example.com");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newSecret")).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User update = new User();
        update.setPassword("newSecret");

        userService.updateUser(update);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("newEncoded");
    }

    @Test
    void updateUser_doesNotEncodePasswordWhenPasswordIsBlank() {
        mockAuthenticatedUser("jane@example.com");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User update = new User();
        update.setPassword("  ");

        userService.updateUser(update);

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deleteUser_deletesAuthenticatedUser() {
        mockAuthenticatedUser("jane@example.com");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        userService.deleteUser();

        verify(userRepository).delete(user);
    }

    @Test
    void getLoggedInUser_returnsAuthenticatedUser() {
        mockAuthenticatedUser("jane@example.com");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        User result = userService.getLoggedInUser();

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getLoggedInUser_withUnknownEmail_throwsResourceNotFoundException() {
        mockAuthenticatedUser("ghost@example.com");
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getLoggedInUser())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost@example.com");
    }

    private void mockAuthenticatedUser(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}

