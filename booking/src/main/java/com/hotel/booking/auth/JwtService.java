package com.hotel.booking.auth;
import com.hotel.booking.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

/*
A MAC uses a shared secret to authenticate and protect
message integrity, while a digital signature uses asymmetric
cryptography to provide integrity, authentication,
and non-repudiation.
*/
@Component
public class JwtService {

    private final String SECRET_KEY = "secret_key_123456098340297345824750245";


    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail()) //add user email to token payload
                .claim("role", user.getRole().name()) // used later for authorization
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) //signs token with secret key. prevents token tampering
                //Hash-based Message Authentication Code
                //signs it with HMAC + SecureHashingAlgorithm
                //guarantees integrity + authenticity
                //No one can modify the token & ensure it came from a trusted source
                //for a single backend system it is better to use MAC
                //for Microservices, Production systems --> Digital Signatures (Public/Private Keys)
                .compact();
                //when compact is called --> uses header + payload to generate the signature that will be added at the end of the token
    }
    //Extracts user email from JWt token payload
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("Invalid token: " + e.getMessage());
        }
        return false;
    }

}
