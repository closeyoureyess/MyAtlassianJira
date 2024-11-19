package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     Сервис для генерации и валидации JWT-токенов
 * </pre>
 */
@Service
public class JwtService {

    private static final long VALIDTIME = TimeUnit.MINUTES.toMillis(20);

    /**
     * Генерирует JWT-токен для заданного пользователя.
     *
     * @param userDetails детали пользователя
     * @return JWT-токен в виде строки
     */
    public String generateToken(UserDetails userDetails){
        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("iss", "https://effective-mobile.ru/");
        return Jwts.builder()
                .claims(claimsMap)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDTIME)))
                .signWith(generateKey())
                .compact();
    }

    /**
     * Генерирует секретный ключ для подписи JWT-токенов.
     *
     * @return секретный ключ
     */
    private SecretKey generateKey(){
        byte[] decodedKey = Base64.getDecoder().decode(ConstantsClass.SECRETKEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Извлекает email пользователя из JWT-токена.
     *
     * @param jwt JWT-токен
     * @return email пользователя
     */
    public String extractEmailUser(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Получает Claims из JWT-токена.
     *
     * @param jwt JWT-токен
     * @return объект Claims
     */
    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * Проверяет валидность JWT-токена по сроку действия.
     *
     * @param jwt JWT-токен
     * @return true, если токен валиден; false в противном случае
     */
    public boolean isTokenValid(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
