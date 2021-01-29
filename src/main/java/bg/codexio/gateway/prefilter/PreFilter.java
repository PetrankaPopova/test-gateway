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
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String authHeader = request.getHeader("Authorization");
        Long kur = 0L;
        if (authHeader != null && this.jwtUtils.validateJwtToken(authHeader)) {
            System.out.println("i got id from jwt");
             kur = jwtUtils.getUserIdFromJwtToken(authHeader);
        }
        return kur;
    }
}
