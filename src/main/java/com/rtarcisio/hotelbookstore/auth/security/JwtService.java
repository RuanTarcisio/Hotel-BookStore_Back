package com.rtarcisio.hotelbookstore.auth.security;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth.dtos.AccessToken;
import com.rtarcisio.hotelbookstore.shared.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKeyGenerator keyGenerator;

    public AccessToken generateToken(AuthUser user) {
        var key = keyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts.builder()
                .claims(claims)
                .subject(user.getUserId())
                .expiration(expirationDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        return new AccessToken(token);
    }

    private Date generateExpirationDate() {
        int expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", user.getName());
        claims.put("email", user.getEmail());

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        return claims;
    }

    public Long getIdFromToken(String tokenJwt) {

        try {
            JwtParser build = Jwts.parser().verifyWith(keyGenerator.getKey()).build();

            Jws<Claims> jwsClaims = build.parseSignedClaims(tokenJwt);
            Claims claims = jwsClaims.getPayload();
            return Long.parseLong(claims.getSubject());
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    public String getClaim(String tokenJwt, String reference) {

        try {
            JwtParser build = Jwts.parser().verifyWith(keyGenerator.getKey()).build();

            Jws<Claims> jwsClaims = build.parseSignedClaims(tokenJwt);
            Claims claims = jwsClaims.getPayload();
            return claims.get(reference, String.class);
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("invalid claim");
        }
    }
}
