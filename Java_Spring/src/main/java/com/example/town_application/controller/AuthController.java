package com.example.town_application.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.example.bookstore.payload.request.LoginRequest;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.town_application.kubik.JwtResponse;
import com.example.town_application.kubik.JwtUtils;
import com.example.town_application.kubik.SignupRequest;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UsersRepository userRepository;
    PersonalDataRepository personalDataRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;


    @Autowired
    public void setPersonalDataRepository(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUsersRepository(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String jwt = jwtUtils.generateJwtToken(authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getLogin(),
                                loginRequest.getPassword())));

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User already exist!"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(signUpRequest.getPesel())
                .orElseThrow(() -> new UsernameNotFoundException("Person Not Found with pesel: " + signUpRequest.getPesel()));

        if(userRepository.existsByPermissionLevelAndPersonalDataForUsers(signUpRequest.getRole(), personalData)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Account for this person with this permision already exists"));
        }

        Users user = new Users(
                    signUpRequest.getLogin(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getRole(),
                    personalData);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
