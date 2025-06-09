package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.auth.JwtUtil;
import com.sekretowicz.gym_crm.auth.LoginAttemptService;
import com.sekretowicz.gym_crm.dto_legacy.ChangePasswordDto;
import com.sekretowicz.gym_crm.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Authentication and password operations")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            loginAttemptService.reset(username);

            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException ex) {
            loginAttemptService.registerFailure(username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
        } catch (LockedException ex) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Account is temporarily locked");
        }
    }

    //4. Change Login (PUT method)
    //It's called so in documentation, but actually it changes password
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto dto) {
        if(service.changePassword(dto)) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
