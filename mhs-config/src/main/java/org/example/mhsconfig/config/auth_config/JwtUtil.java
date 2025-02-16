package org.example.mhsconfig.config.auth_config;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import org.example.mhcommon.core.json.JsonObject;
import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.example.mhscommons.data.tables.pojos.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.example.mhcommon.data.constant.message.jwt.JwtMessageConstant.*;

@Component
@Slf4j
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    private static final long EXPIRE_DURATION =  24 * 60 * 60 * 1000;
    public String generateToken(User user, List<String> roles){
        UserAuthorDTO userDetail = new UserAuthorDTO();
        userDetail.setUserId(user.getId());
        userDetail.setRoles(roles);
        Date now = new Date();
        Date expireDuration = new Date(now.getTime() + EXPIRE_DURATION);
        return Jwts.builder()
                .setId(userDetail.getUserId().toString())
                .setSubject(JsonObject.mapFrom(userDetail).encode())
                .setIssuedAt(now)
                .setExpiration(expireDuration)
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }
    public boolean validateAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException ex) {
            log.error("[ERROR] validateAccesstoken {} " + JWT_EXPIRED, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("[ERROR] validateAccesstoken {} " +TOKEN_NULL, ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("[ERROR] validateAccesstoken {} " +TOKEN_INVALID, ex);
        } catch (UnsupportedJwtException ex) {
            log.error("[ERROR] validateAccesstoken {} " +TOKEN_NOT_SUPPORTED, ex);
        } catch (SignatureException ex) {
            log.error("[ERROR] validateAccesstoken {} " +SIGNATURE_INVALID);
        }
        return false;
    }
    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }
    public Claims parseClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    public String refreshToken(String token){
        final Claims claims = parseClaims(token);
        final long EXPIRATION = 1 * 24 * 60 * 60 * 1000; // 1 day in milliseconds
        Date now = new Date();
        Date expireTime = new Date(now.getTime() +EXPIRATION);

        Claims newClaims = Jwts.claims()
                .setSubject(claims.getSubject())
                .setExpiration(expireTime)
                .setIssuedAt(now)
                .setId(claims.getId());
        claims.forEach(newClaims::put);
        return Jwts.builder()
                .setClaims(newClaims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }
}
