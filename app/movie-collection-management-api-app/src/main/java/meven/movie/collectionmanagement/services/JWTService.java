package meven.movie.collectionmanagement.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author meven
 */
@Service
public class JWTService {

  private final JWTVerifier jwtVerifier;

  private final Function<String, String> jwtEncoder;

  public JWTService(@Value("jwt.secret") String jwtSecret, @Value("jwt.issuer") String jwtIssuer) {
    this.jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer(jwtIssuer).build();
    this.jwtEncoder =
        username -> JWT.create().withSubject(username).withIssuer(jwtIssuer).sign(Algorithm.HMAC256(jwtSecret));
  }

  public String extractUsername(String token) {
    DecodedJWT decodedJWT = jwtVerifier.verify(token);
    return decodedJWT.getSubject();
  }

  public String generateToken(Authentication authentication) {
    return jwtEncoder.apply(authentication.getName());
  }
}
