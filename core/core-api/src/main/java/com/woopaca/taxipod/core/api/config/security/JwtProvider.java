package com.woopaca.taxipod.core.api.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        algorithm = Algorithm.HMAC256(jwtProperties.getClientSecret());
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.getIssuer())
                .build();
    }

    public String generateAccessToken(String subject) {
        return generate(subject, jwtProperties.getAccessTokenExpiry());
    }

    public String generateRefreshToken(String subject) {
        return generate(subject, jwtProperties.getRefreshTokenExpiry());
    }

    private String generate(String subject, long expiry) {
        Date currentDate = new Date();
        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(currentDate)
                .withExpiresAt(currentDate.toInstant().plusSeconds(expiry))
                .withSubject(subject)
                .sign(algorithm);
    }

}
