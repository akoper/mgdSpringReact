package com.example.mgdSpringReact.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final long expirationSeconds;

    public JwtService(JwtEncoder encoder,
                      JwtDecoder decoder,
                      @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        var builder = JwtClaimsSet.builder()
                .issuer("mgdSpringReact")
                .issuedAt(now)
                .expiresAt(now.plus(expirationSeconds, ChronoUnit.SECONDS))
                .subject(subject)
                .claims(map -> map.putAll(claims));
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

    public Jwt decode(String token) {
        return decoder.decode(token);
    }
}
