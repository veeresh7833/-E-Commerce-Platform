package com.example.userserviceaprbatch24.services;

import com.example.userserviceaprbatch24.models.Token;
import com.example.userserviceaprbatch24.models.User;
import com.example.userserviceaprbatch24.repos.TokenRepo;
import com.example.userserviceaprbatch24.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepo userRepo;
    private final TokenRepo tokenRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepo userRepo,
                       TokenRepo tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String email, String name, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        return userRepo.save(user);
    }

    public User validateToken(String tokenvalue) {
        Optional<Token> token =
                tokenRepository.findByValueAndExpiryAtGreaterThan(tokenvalue, new Date());
        if(token.isEmpty()) {
            throw new RuntimeException("InvalidToken");
        }

        return token.get().getUser();
    }

    public Token login(String email, String password) {
        // 1. Verify if the User Exists
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found for email" + email);
        }

        // 2. Verify the password
        if(!bCryptPasswordEncoder.matches(password, user.get().getHashedPassword())) {
            throw new RuntimeException("Password not matching");
        }

        // 3. Generate Token
        Token token = generateToken(user.get());
        tokenRepository.save(token);

        return token;
    }

    private Token generateToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(10));
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiryAt(expiryDate);
        return token;
    }
}
