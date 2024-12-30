package com.example.userservice.controllers;

import com.example.userservice.dtos.LoginRequestDto;
import com.example.userservice.dtos.SignUpRequestDto;
import com.example.userservice.dtos.UserDto;
import com.example.userservice.dtos.ValidateRequestDto;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userservice;

    public UserController(UserService userService) {
        this.userservice = userService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto requestDto) {
        User user =
                userservice.signUp(requestDto.getEmail(), requestDto.getName(), requestDto.getPassword());
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto) {
        Token token =
                userservice.login(requestDto.getEmail(), requestDto.getPassword());
        return token;
    }

    @PostMapping("/validate/{token}")
    public UserDto validate(@PathVariable String token) {
        try {
            return UserDto.from(userservice.validateToken(token));
        } catch (Exception e) {
            return null;
        }
    }
}
