package kz.adem.userservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component

public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private Long accessTokenExpirationDate;
    @Value("${app.refresh-token-expiration-milliseconds}")
    private Long refreshTokenExpirationDate;



    public String generateToken(Authentication authentication,Long id){
        String username = authentication.getName();
//        Long id = userService.getUserIdByUsername(username);
        Map<String,Object> claims = new HashMap<>();
        claims.put("token_type","access_token");
        claims.put("user_id",id);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpirationDate))
                .signWith(key())
                .compact();
        String tokenWithClaims = addClaimsToToken(token,claims);
        return tokenWithClaims;
    }
    public String generateRefreshToken(Authentication authentication,Long id){
        String username = authentication.getName();
        Map<String,Object> claims = new HashMap<>();
        claims.put("token_type","refresh_token");
        claims.put("user_id",id);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+refreshTokenExpirationDate))
                .signWith(key())
                .compact();
        String tokenWithClaims = addClaimsToToken(token,claims);
        return tokenWithClaims;
    }
    public String addClaimsToToken(String token, Map<String,Object> additionalClaims){
        Claims claims = extractAllClaims(token);
        claims.putAll(additionalClaims);
        String tokenWithClaims = Jwts.builder()
                .setClaims(claims)
                .signWith(key())
                .compact();
        return tokenWithClaims;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    private Claims extractAllClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
    public String extractTokenType(String token){
        return extractClaim(token,claims -> claims.get("token_type",String.class));
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
