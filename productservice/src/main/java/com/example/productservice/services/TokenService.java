package com.example.productservice.services;

import com.example.productservice.dtos.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {
    private RestTemplate restTemplate;

    public TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateToken(String token) {
        UserDto userDto = restTemplate.postForObject("http://localhost:9000/users/validate/" +  token, null, UserDto.class);
        return userDto != null;
    }

}
