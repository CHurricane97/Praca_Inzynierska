package com.example.town_application.config;

import com.example.town_application.model.Users;
import com.example.town_application.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    //TODO: Take care of this (testing purposes only)
    @Bean
    public CommandLineRunner createAdminAccount(AuthService authService) {
        return args -> {
            try {
                //authService.registerUser(new Users("admin1", 2, "abba"));
            }
            catch (IllegalArgumentException ignored) {
            }
        };
    }
}
