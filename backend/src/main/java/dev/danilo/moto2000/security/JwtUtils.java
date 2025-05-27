package dev.danilo.moto2000.security;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private SecretKey key;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 dias por padrão
    private Long refreshExpiration;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    private void init() {
        byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyByte, "HmacSHA256");
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh") // Identifica como refresh token
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        return extractUsername(refreshToken);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (isTokenExpired(refreshToken)) {
                return false;
            }

            String tokenType = extractClaims(refreshToken, claims -> claims.get("type", String.class));
            return "refresh".equals(tokenType);

        } catch (Exception e) {
            log.warn("Refresh token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public Date getExpirationDate(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public Date getIssuedAt(String token) {
        return extractClaims(token, Claims::getIssuedAt);
    }
}
