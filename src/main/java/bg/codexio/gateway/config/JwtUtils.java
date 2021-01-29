package bg.codexio.gateway.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {


    private final String jwtSecret;

    private final int jwtExpirationMs;


    public JwtUtils(
            @Value("${example.app.jwtSecret}") String jwtSecret,
            @Value("${example.app.jwtExpirationMs}") int jwtExpirationMs
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }


    public Long getUserIdFromJwtToken(String token){
        return Long.parseLong(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            String subs = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getSubject();
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ignored) {
        }

        return false;
    }
}
