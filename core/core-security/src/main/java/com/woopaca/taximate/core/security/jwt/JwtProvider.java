package com.woopaca.taximate.core.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.woopaca.taximate.core.domain.auth.token.JwtProviderProxy;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider implements JwtProviderProxy {

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

    @Override
    public String issueAccessToken(User principal) {
        Date currentDate = new Date();
        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(currentDate)
                .withExpiresAt(currentDate.toInstant().plusSeconds(jwtProperties.getAccessTokenExpiry()))
                .withSubject(principal.getEmail())
                .sign(algorithm);
    }

    @Override
    public String verify(String accessToken) {
        return jwtVerifier.verify(accessToken)
                .getSubject();
    }
}
