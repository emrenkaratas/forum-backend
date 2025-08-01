package com.example.forum.service;

import com.example.forum.dto.LoginRequest;
import com.example.forum.dto.LoginResponse;
import com.example.forum.dto.UserRequest;
import com.example.forum.model.User;
import com.example.forum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest req;
    private LoginRequest loginReq;
    private User existing;

    @BeforeEach
    void setUp() {
        req = new UserRequest();
        req.setUsername("alice");
        req.setPassword("plain");
        req.setEmail("alice@example.com");
        req.setName("Alice");
        req.setSurname("Anderson");
        req.setBirthdate(LocalDate.of(1990, 5, 4));

        loginReq = new LoginRequest();
        loginReq.setUsername("alice");
        loginReq.setPassword("plain");

        existing = User.builder()
                .id(1L)
                .username("alice")
                .password("encodedPw")
                .email("alice@example.com")
                .name("Alice")
                .surname("Anderson")
                .birthdate(LocalDate.of(1990, 5, 4))
                .build();
    }

    @Test
    void getAllUsers_returnsAll() {
        when(userRepository.findAll()).thenReturn(List.of(existing));

        List<User> all = userService.getAllUsers();

        assertThat(all).containsExactly(existing);
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        User u = userService.getUserById(1L);

        assertThat(u).isSameAs(existing);
    }

    @Test
    void getUserById_notFound_throws404() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.getUserById(99L),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void register_success() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("plain")).thenReturn("encodedPw");
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.register(req);

        assertThat(saved.getUsername()).isEqualTo("alice");
        assertThat(saved.getPassword()).isEqualTo("encodedPw");
        verify(userRepository).existsByUsername("alice");
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(saved);
    }

    @Test
    void register_usernameTaken_throws400() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.register(req),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_success() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("plain", "encodedPw"))
                .thenReturn(true);

        LoginResponse resp = userService.login(loginReq);

        assertThat(resp.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_unknownUser_throws401() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.login(loginReq),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_badPassword_throws401() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("plain", "encodedPw"))
                .thenReturn(false);

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.login(loginReq),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void updateUser_success_rehashesPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpw")).thenReturn("newEncoded");
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserRequest update = new UserRequest();
        update.setUsername("alice2");
        update.setPassword("newpw");
        update.setEmail("a2@example.com");
        update.setName("Alice2");
        update.setSurname("A.");
        update.setBirthdate(LocalDate.of(1991, 6, 5));

        User updated = userService.updateUser(1L, update);

        assertThat(updated.getUsername()).isEqualTo("alice2");
        assertThat(updated.getPassword()).isEqualTo("newEncoded");
        assertThat(updated.getEmail()).isEqualTo("a2@example.com");
        verify(passwordEncoder).encode("newpw");
    }

    @Test
    void updateUser_notFound_throws404() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.updateUser(99L, req),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throws404() {
        when(userRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException ex = catchThrowableOfType(
                () -> userService.deleteUser(99L),
                ResponseStatusException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
