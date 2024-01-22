package com.project.daybart.demo.Users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Users users) {
        return usersService.createUser(users);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestHeader Users users) {
        return usersService.deleteUser(users);
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(@RequestBody Users users) {
        return usersService.updateUserByID(users);
    }

    @GetMapping
    public ResponseEntity<Users> searchUser(@RequestParam String username) {
        return usersService.findByUsername(username);
    }

}
