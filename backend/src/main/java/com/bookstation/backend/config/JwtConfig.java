package com.bookstation.backend.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.secret:mySecretKeymySecretKeymySecretKeymySecretKeymySecretKey}")
    private String secret;

    private Key jwtKey;

    @PostConstruct
    public void init() {
        this.jwtKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Bean
    public Key jwtKey() {
        return jwtKey;
    }
}