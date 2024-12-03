package com.example.study_org_server.util;

import com.example.study_org_server.exception.AuthorityError;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    @Getter
    private Long jwtExpirationMs = 3000L * 60 * 60;
    private String jwtSecretKey = "3yqHrQ5Sw7+z4LvKyJZ+n+VkDYHnb5TzEEBcxhZT3bg=";
    private SecretKey getSignWithKey(){
        byte[] decodedKey = Decoders.BASE64.decode(jwtSecretKey);
        //HS256アルゴリズムに対応する秘密鍵の生成
        return Keys.hmacShaKeyFor(decodedKey);
    }
    public String getJwtFromRequest(HttpServletRequest req){
        if(req.getCookies()==null) return null;
        return  Arrays.stream(req.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

//        return "admin@example.com";
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
    }


    //JWTの発行
    public String generateJWT(Authentication auth){
        String email = auth.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("roles", auth.getAuthorities()); // ユーザーの権限
        return Jwts
                .builder()
                .claims(additionalClaims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignWithKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validate(String jwt) {
        try{
            Jwts.parser()
                    .verifyWith(getSignWithKey())
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        }catch(IllegalArgumentException | JwtException exception){
            return false;
        }
    }

    //JWTの解読
    //JWT -> Email
    public String getEmailFromJWT(String jwt){
        return Jwts.parser()
                .verifyWith(getSignWithKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromJWT(String jwt){
        return Jwts.parser()
                .verifyWith(getSignWithKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .get("roles").toString();

    }




}
