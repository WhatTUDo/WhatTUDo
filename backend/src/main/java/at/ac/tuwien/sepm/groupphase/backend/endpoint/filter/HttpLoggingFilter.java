package at.ac.tuwien.sepm.groupphase.backend.endpoint.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class HttpLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("[" + ((HttpServletRequest) req).getMethod() + "] " + ((HttpServletRequest) req).getServletPath());
        filterChain.doFilter(req, servletResponse);
    }
}
