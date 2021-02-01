package bg.codexio.gateway.prefilter;

import bg.codexio.gateway.config.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(PreFilter.class);
    private final JwtUtils jwtUtils;

    public PreFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String authHeader = request.getHeader("Authorization");
        String header = "";
        if (authHeader != null && this.jwtUtils.validateJwtToken(authHeader)) {
            System.out.println("i got id from jwt");
             header = jwtUtils.getUserIdFromJwtToken(authHeader);
            System.out.println();
            requestContext.addZuulRequestHeader("Id", header);
//            return header;
//             PROBVAI BEZ RETURN TYPE
        }
        return null;
    }

//    public Object run() throws ZuulException {
//
//        final RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//
//        String authorizationHeader = request.getHeader("Authorization");
//        String tokenFromHeader = getTokenFromAuthorizationHeader(authorizationHeader);
//        DecodedJWT decodedToken = jwtService.verifyToken(tokenFromHeader);
//        String userId = decodedToken.getKeyId();
//        if (!decodedToken.equals(null)) {
//            addClaimsToRequestAsHeaders(requestContext, userId);
//        }
//        else {
//            throw new ZuulException("Invalid token", HttpStatus.UNAUTHORIZED.value(), "Token Signature is invalid.");
//        }
//
//        return null;
//    }
//
//    public static String getTokenFromAuthorizationHeader(String header) {
//        String token = header.replace("Bearer ", "");
//        return token.trim();
//    }
//
//    private void addClaimsToRequestAsHeaders(RequestContext requestContext, String id) {
//        requestContext.addZuulRequestHeader(X_USER_ID, id);
//        requestContext.addZuulRequestHeader(X_USER_SCOPES, String.valueOf(userRoles));
//    }
}
