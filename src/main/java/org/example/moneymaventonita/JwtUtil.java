package org.example.moneymaventonita;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // ✅ 1 Hour Expiry

    // ✅ Load secret key from application.properties OR generate if missing
    public JwtUtil(@Value("${jwt.secret:}") String secret) {
        if (secret == null || secret.isEmpty()) {
            this.SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String base64Key = Encoders.BASE64.encode(SECRET_KEY.getEncoded());
            System.out.println("⚠️ Generated Secret Key (Store this in application.properties!): " + base64Key);
        } else {
            this.SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        }
    }

    // ✅ Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // ✅ 1-hour expiration
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract email from token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ Validate Token (handles expired tokens)
    public boolean validateToken(String token, String email) {
        try {
            return email.equals(extractEmail(token)) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token expired for user: " + email);
            return false; // ✅ Token expired
        } catch (JwtException e) {
            System.out.println("❌ Invalid token: " + e.getMessage());
            return false; // ✅ Invalid token
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // ✅ Expired token
        }
    }
}

