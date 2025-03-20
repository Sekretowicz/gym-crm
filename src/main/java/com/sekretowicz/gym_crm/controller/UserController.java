package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.ChangePasswordDto;
import com.sekretowicz.gym_crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    //3. Login (GET method)
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        //TODO: Это все переделаем, потом сделаем обработку исключений.
        if (service.login(username, password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    //4. Change Login (PUT method)
    //It's called so in documentation, but actually it changes password
    @PutMapping()
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto dto) {

        //TODO: Если этот метод будет бросать исключения, обработаем его здесь
        if(service.changePassword(dto)) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

}
