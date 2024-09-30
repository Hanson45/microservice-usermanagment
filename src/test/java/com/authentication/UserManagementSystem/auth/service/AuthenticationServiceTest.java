package com.authentication.UserManagementSystem.auth.service;

import com.authentication.UserManagementSystem.auth.exception.InvalidEmailException;
import com.authentication.UserManagementSystem.auth.exception.InvalidPasswordException;
import com.authentication.UserManagementSystem.auth.exception.UserAlreadyRegisteredException;
import com.authentication.UserManagementSystem.auth.exception.UserNotFoundException;
import com.authentication.UserManagementSystem.auth.mapper.PhoneMapper;
import com.authentication.UserManagementSystem.auth.model.dto.LoginResponse;
import com.authentication.UserManagementSystem.auth.model.dto.PhoneDto;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterResponse;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterUserDto;
import com.authentication.UserManagementSystem.auth.model.entity.Phone;
import com.authentication.UserManagementSystem.auth.model.entity.User;
import com.authentication.UserManagementSystem.auth.repository.PhoneRepository;
import com.authentication.UserManagementSystem.auth.repository.UserRepository;
import com.authentication.UserManagementSystem.auth.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication auth;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    void register_test_with_UserAlreadyRegisteredException() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("example@example.com");
        newUser.setPassword("12345678");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyRegisteredException.class, () -> authService.register(newUser));
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_regex_validation_EmailWrongFormat() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("example");
        newUser.setPassword("1aAs2daa");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        InvalidEmailException ex = assertThrows(InvalidEmailException.class, () -> authService.register(newUser));

        assertEquals("Email invalid", ex.getMessage());
    }

    @Test
    void test_regex_validation_PassWithExtraUppercase() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2daaA");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class, () -> authService.register(newUser));

        assertEquals("Password invalid", ex.getMessage());
    }

    @Test
    void test_regex_validation_passWith7Character() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2da");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class, () -> authService.register(newUser));

        assertEquals("Password invalid", ex.getMessage());
    }

    @Test
    void test_regex_validation_passWith13Character() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2daadsxcv");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class, () -> authService.register(newUser));

        assertEquals("Password invalid", ex.getMessage());
    }

    @Test
    void test_regex_validation_passWith3numbers() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2d2a");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class, () -> authService.register(newUser));

        assertEquals("Password invalid", ex.getMessage());
    }

    @Test
    void test_register_without_names_and_phones() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2daa");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("passEncoded");
        when(jwtService.generateToken(any(User.class))).thenReturn("JwtToken");

        User savedUser = User.builder()
                .id(UUID.fromString("c386c030-497b-44e9-8efd-7af711530b3a"))
                .name(newUser.getName())
                .email(newUser.getEmail())
                .password("passEncoded")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = authService.register(newUser);

        assertNotNull(response);
        assertEquals(response.getId(), UUID.fromString("c386c030-497b-44e9-8efd-7af711530b3a"));
        assertNull(newUser.getName());
        assertNull(newUser.getPhones());
        assertEquals("JwtToken", response.getToken());
        assertTrue(response.getIsActive());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getLastLogin());
    }

    @Test
    void test_register_with_name_and_phones() {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setEmail("validemail@example.com");
        newUser.setPassword("1aAs2daa");

        // Agregar una lista de teléfonos con el formato especificado
        List<PhoneDto> phones = List.of(new PhoneDto(34343874L, 11, "countrycode"));
        newUser.setPhones(phones);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("passEncoded");
        when(jwtService.generateToken(any(User.class))).thenReturn("JwtToken");

        User savedUser = User.builder()
                .id(UUID.fromString("c386c030-497b-44e9-8efd-7af711530b3a"))
                .name(newUser.getName())
                .email(newUser.getEmail())
                .password("passEncoded")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        List<Phone> mappedPhones = PhoneMapper.toEntity(phones);

        // Mockear la llamada al repositorio de teléfonos
        when(phoneRepository.saveAll(anyList())).thenReturn(mappedPhones);

        // Llamar al método register
        RegisterResponse response = authService.register(newUser);

        // Verificaciones
        assertNotNull(response);
        assertEquals("JwtToken", response.getToken());
        assertTrue(response.getIsActive());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getLastLogin());
        assertEquals(newUser.getPhones(), phones);
    }

    @Test
    void test_login_successful(){
        when(auth.getName()).thenReturn("validemail@example.com");

        User savedUser = User.builder()
                .id(UUID.fromString("c386c030-497b-44e9-8efd-7af711530b3a"))
                .name("Test User")
                .email("validemail@example.com")
                .password("passEncoded")
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .phones(List.of(
                        Phone.builder()
                                .number(343434123L)
                                .cityCode(11)
                                .countryCode("countrycode")
                                .build()
                ))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(savedUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0);
            updatedUser.setLastLogin(LocalDateTime.now());
            return updatedUser;
        });

        when(jwtService.generateToken(any(User.class))).thenReturn("newJwtToken");

        LoginResponse response = authService.login(auth);

        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(savedUser.getCreatedAt(), response.getCreated());
        assertNotNull(response.getLastLogin());
        assertNotEquals(savedUser.getCreatedAt(), response.getLastLogin());
        assertEquals("newJwtToken", response.getToken());
        assertTrue(response.getIsActive());
        assertEquals(savedUser.getName(), response.getName());
        assertEquals(savedUser.getEmail(), response.getEmail());
        assertEquals(savedUser.getPassword(), response.getPassword());
        assertNotNull(response.getPhones());


        verify(userRepository).findByEmail("validemail@example.com");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(savedUser);
    }

    @Test
    void test_login_user_not_found() {
        when(auth.getName()).thenReturn("validemail@example.com");


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> authService.login(auth));

        assertEquals("User not found", ex.getMessage());
    }
}
