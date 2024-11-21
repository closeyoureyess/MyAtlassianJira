package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtServiceImpl implements JwtService{

    private static final long VALIDTIME = TimeUnit.MINUTES.toMillis(20);

    @Override
    public String generateToken(UserDetails userDetails){
        log.info("Метод generateToken()");
        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("effectiveMobile", "https://effective-mobile.ru/");
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
        log.info("Метод generateKey()");
        byte[] decodedKey = Base64.getDecoder().decode(ConstantsClass.SECRETKEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    @Override
    public String extractEmailUser(String jwt){
        log.info("Метод extractEmailUser()");
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
        log.info("Метод getClaims()");
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    @Override
    public boolean isTokenValid(String jwt){
        log.info("Метод isTokenValid()");
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
