package com.example.town_application.controller;

import com.example.town_application.model.AuthToken;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping("/login")
    @GetMapping
    public ResponseEntity<Object> login(@RequestParam() String email, @RequestParam() String password) {
        Map<String, Object> resp = new HashMap<>();

        try {
            AuthToken token = authService.login(email, password, 0);
            resp.put("status", "success");
            resp.put("token", token);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("status", "error");
            resp.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(resp);
        }
    }

    @RequestMapping("/admin")
    @GetMapping
    public ResponseEntity<Object> adminLogin(@RequestParam(required = true) String email, @RequestParam(required = true) String password) {
        Map<String, Object> resp = new HashMap<>();

        try {
            AuthToken token = authService.login(email, password, 1);
            resp.put("status", "success");
            resp.put("token", token);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("status", "error");
            resp.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(resp);
        }
    }

    @RequestMapping("/register")
    @PutMapping
    public ResponseEntity<Object> register(@RequestBody Users user) {
        Map<String, Object> resp = new HashMap<>();
        try {
            if (user.getPermissionLevel() != 0)
                throw new IllegalArgumentException("Creating admin accounts is not allowed.");
            authService.registerUser(user);
            resp.put("status", "success");
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            resp.put("status", "error");
            resp.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(resp);
        }

    }

    @RequestMapping("/revoke")
    @DeleteMapping
    public ResponseEntity<Object> revoke(@RequestParam(name = "token") String token) {
        Map<String, Object> resp = new HashMap<>();
        try {
            authService.revoke(token);
            resp.put("status", "success");
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            resp.put("status", "error");
            resp.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(resp);
        }

    }
}

