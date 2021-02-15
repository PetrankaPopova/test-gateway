package bg.codexio.gateway.prefilter;

import bg.codexio.gateway.config.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class PreFilter extends ZuulFilter {

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
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        return !request.getRequestURI().startsWith("/auth");
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String authHeader = request.getHeader("Authorization").replace("Bearer ", "");

        if (!authHeader.equals("") && this.jwtUtils.validateJwtToken(authHeader)) {
            String header = jwtUtils.getUserIdFromJwtToken(authHeader.replace("Bearer ", ""));
            requestContext.addZuulRequestHeader("Id", header);
        }

        return null;
    }
}
