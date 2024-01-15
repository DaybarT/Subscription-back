package com.project.daybart.demo.Users;

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
    public void createUser(@RequestBody Users users) {
        usersService.createUser(users);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader Users users) {
        usersService.deleteUser(users);
    }

    @PatchMapping
    public void updateUser(@RequestBody Users users) {
        usersService.updateUserByID(users);
    }

    @GetMapping
    public Users searchUser(@RequestParam String username) {
        return usersService.findByUsername(username).orElse(null);
    }

}
