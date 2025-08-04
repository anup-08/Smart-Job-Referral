package com.smartJob.jwtUtil;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public SecretKey getSignIn(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String userName, List<String> roles){
        return Jwts.builder()
                .setSubject(userName)
                .claim("role" , roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+3*60*1000))
                .signWith(getSignIn())
                .compact();

    }

    public Claims claims(String token){
        JwtParser parser = Jwts.parser().setSigningKey(getSignIn()).build();
        Jws<Claims> claimsJwt = parser.parseClaimsJws(token);
        return claimsJwt.getBody();
    }

    public String extractUserName(String token){
        return claims(token).getSubject();
    }

    public List<String> extractRole(String token){
        return (List<String>) claims(token).get("role");
    }

    public boolean isExpired(String token){
        return (claims(token).getExpiration().before(new Date()));
    }

    public boolean validateToken(String token , String userName){
        return extractUserName(token).equals(userName) && !isExpired(token);
    }
}
