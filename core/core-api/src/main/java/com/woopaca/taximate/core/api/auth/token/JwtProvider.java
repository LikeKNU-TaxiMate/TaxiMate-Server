package com.woopaca.taximate.core.api.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.woopaca.taximate.core.api.user.domain.User;
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

    public String issueAccessToken(User principal) {
        Date currentDate = new Date();
        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(currentDate)
                .withExpiresAt(currentDate.toInstant().plusSeconds(jwtProperties.getAccessTokenExpiry()))
                .withSubject(principal.getEmail())
                .sign(algorithm);
    }

    public String verify(String accessToken) {
        return jwtVerifier.verify(accessToken)
                .getSubject();
    }
}
