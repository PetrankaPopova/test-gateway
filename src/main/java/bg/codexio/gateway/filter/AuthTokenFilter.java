package bg.codexio.gateway.filter;

import bg.codexio.gateway.config.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;


@Component
public class AuthTokenFilter {

    private final JwtUtils jwtUtils;

    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    public String findIdFromJwt(HttpServletRequest request) {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && this.jwtUtils.validateJwtToken(jwt)) {
                System.out.println("i got id from jwt");
                return jwtUtils.getUserIdFromJwtToken(jwt);
            }
        } catch (Exception e) {

            System.out.println("Cannot set user authentication: ");
        }
        return null;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

}
