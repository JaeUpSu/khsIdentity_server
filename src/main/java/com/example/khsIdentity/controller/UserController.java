package com.example.khsIdentity.controller;

import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private Mapper mapper = new Mapper();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        UserDTO joinUser = userService.join(user);
        return ResponseEntity.ok(joinUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody String userId, String password) {
        try {
            UserDTO loggedInUser = userService.login(userId, password);
            return ResponseEntity.ok(loggedInUser);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO user = userService.getLoggedInUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findUsers()
                .stream().map(user -> mapper.convertUserToDto(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        UserDTO user = new Mapper().convertUserToDto(userService.findOne(id).get());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<UserDTO> getUserByUserId(@PathVariable String userId) {
        UserDTO user = new Mapper().convertUserToDto(userService.findOneByUserId(userId).get());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = new Mapper().convertUserToDto(userService.findOneByEmail(email).get());
        return ResponseEntity.ok(user);
    }
}
