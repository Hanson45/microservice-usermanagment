package com.authentication.UserManagementSystem.auth.service;

import com.authentication.UserManagementSystem.auth.exception.InvalidEmailException;
import com.authentication.UserManagementSystem.auth.exception.InvalidPasswordException;
import com.authentication.UserManagementSystem.auth.exception.UserAlreadyRegisteredException;
import com.authentication.UserManagementSystem.auth.exception.UserNotFoundException;
import com.authentication.UserManagementSystem.auth.mapper.PhoneMapper;
import com.authentication.UserManagementSystem.auth.model.dto.LoginResponse;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterResponse;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterUserDto;
import com.authentication.UserManagementSystem.auth.model.entity.Phone;
import com.authentication.UserManagementSystem.auth.model.entity.User;
import com.authentication.UserManagementSystem.auth.repository.PhoneRepository;
import com.authentication.UserManagementSystem.auth.repository.UserRepository;
import com.authentication.UserManagementSystem.auth.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


        public RegisterResponse register(RegisterUserDto newUser) {
            if(findByEmail(newUser.getEmail()).isPresent()){
                throw new UserAlreadyRegisteredException("Email already registered");
            }

            regexValidation(newUser);

            var user = User.builder()
                    .name(newUser.getName())
                    .email(newUser.getEmail())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .createdAt(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                    .build();

            user = userRepository.save(user);


            if(newUser.getPhones() != null && !newUser.getPhones().isEmpty()){
                User finalUser = user;
                List<Phone> phones = PhoneMapper.toEntity(newUser.getPhones()).stream()
                        .peek(phone -> phone.setUser(finalUser))
                        .toList();
                phoneRepository.saveAll(phones);
             }

            var jwtToken = jwtService.generateToken(user);

            return RegisterResponse.builder()
                    .id(user.getId())
                    .createdAt(user.getCreatedAt())
                    .lastLogin(user.getLastLogin())
                    .token(jwtToken)
                    .isActive(user.getIsActive())
                    .build();
        }


    public LoginResponse login(Authentication auth){

        String email = auth.getName();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        user = userRepository.save(user);

        String newJwt = jwtService.generateToken(user);

        return LoginResponse.builder()
                .id(user.getId())
                .created(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .token(newJwt)
                .isActive(user.getIsActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(PhoneMapper.toDTO(user.getPhones()))
                .build();

    }

    //Metodos privados

    private Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private void regexValidation(RegisterUserDto user){
        if(!user.getEmail().matches("^[a-zA-Z0-9-_.]+@[a-zA-Z0-9-_]+(\\.[a-zA-Z]{2,4}){1,2}$") ){
            throw new InvalidEmailException("Email invalid");
        }
        if(!user.getPassword().matches("^(?=[^A-Z]*[A-Z][^A-Z]*$)(?=(?:[^0-9]*\\d[^0-9]*){2}[^0-9]*$)[a-zA-Z\\d#@$!&*?]{8,12}")){
            throw new InvalidPasswordException("Password invalid");
        }
    }
}
