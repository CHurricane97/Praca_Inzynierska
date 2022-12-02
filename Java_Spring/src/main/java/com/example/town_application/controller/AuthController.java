package com.example.town_application.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import com.example.town_application.WIP.*;
import com.example.town_application.WIP.requests.account.ChangePasswordAdminRequest;
import com.example.town_application.WIP.requests.account.ChangePasswordUserReqest;
import com.example.town_application.WIP.requests.account.LoginRequest;
import com.example.town_application.WIP.requests.account.SignupRequest;
import com.example.town_application.model.LoginRegister;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.repository.LoginRegisterRepository;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import com.example.town_application.service.EvaluationService;
import com.example.town_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UsersRepository userRepository;
    PersonalDataRepository personalDataRepository;
    PasswordEncoder encoder;
    LoginRegisterRepository loginRegisterRepository;
    JwtUtils jwtUtils;
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLoginRegisterRepository(LoginRegisterRepository loginRegisterRepository) {
        this.loginRegisterRepository = loginRegisterRepository;
    }

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

    @PostMapping("/loginUser")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {


        String jwt = jwtUtils.generateJwtToken(authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getLogin(),
                                loginRequest.getPassword())));

        if (!userRepository.existsByLoginAndPermissionLevel(loginRequest.getLogin(), "ROLE_USER")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Admin account not found!"));
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Users users = userRepository.findByLogin(loginRequest.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Can't find user"));
            LoginRegister loginRegister = new LoginRegister(timestamp, users);
            loginRegisterRepository.save(loginRegister);

        }

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {


        String jwt = jwtUtils.generateJwtToken(authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getLogin(),
                                loginRequest.getPassword())));

        if (!userRepository.existsByLoginAndPermissionLevel(loginRequest.getLogin(), "ROLE_ADMIN")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Admin account not found!"));
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Users users = userRepository.findByLogin(loginRequest.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Can't find user"));
            LoginRegister loginRegister = new LoginRegister(timestamp, users);
            loginRegisterRepository.save(loginRegister);

        }
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

        if (userRepository.existsByPermissionLevelAndPersonalDataForUsers(signUpRequest.getRole(), personalData)) {
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

    @PutMapping("/changePasswordUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordUserReqest changePasswordUserReqest, HttpServletRequest request) {
        return userService.changePasswordUser(changePasswordUserReqest.getPassword(), changePasswordUserReqest.getNewpassword(), request);
    }

    @PutMapping("/changePasswordAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePasswordAdmin(@Valid @RequestBody ChangePasswordAdminRequest changePasswordAdminRequest, HttpServletRequest request) {

        return userService.changePasswordAdmin(changePasswordAdminRequest.getLogin(), changePasswordAdminRequest.getNewpassword(), request);
    }


}
