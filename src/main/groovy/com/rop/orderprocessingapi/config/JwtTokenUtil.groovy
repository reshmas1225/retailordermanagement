package com.rop.orderprocessingapi.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import java.util.function.Function

@Component
class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60

    @Value('${jwt.secret.username}')
    String secret

    /**
     * Get username from jwt token
     * @param token
     * @return
     */
    String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject)
    }

    /**
     * Get expiration date from jwt token
     * @param token
     * @return
     */
    Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    /**
     * Get information from the token
     * @param token
     * @param claimsResolver
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    /**
     * Retrieves details from token
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
    }

    /**
     * Checks if the token has expired
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token)
        return expiration.before(new Date())
    }

    /**
     * Generates token for user
     * @param userDetails
     * @return
     */
    String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>()
        return doGenerateToken(claims, userDetails.getUsername())
    }

    /**
     * Signing the JWT token using the HS512 algorithm and secret key
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    /**
     * validate token
     * @param token
     * @param userDetails
     * @return
     */
    Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token)
        return (username == userDetails.getUsername() && !isTokenExpired(token))
    }
}