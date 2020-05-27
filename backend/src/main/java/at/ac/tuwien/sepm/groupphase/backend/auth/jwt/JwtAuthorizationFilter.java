package at.ac.tuwien.sepm.groupphase.backend.auth.jwt;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties, UserRepository userRepository) {
        super(authenticationManager);
        this.securityProperties = securityProperties;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            getAuthToken(request).ifPresent(it -> SecurityContextHolder.getContext().setAuthentication(it));
            chain.doFilter(request, response);
        } catch (IllegalArgumentException | JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid authorization header or token");
            log.debug("Invalid authorization attempt: {}", e.getMessage());
        }
    }

    @Transactional
    Optional<UsernamePasswordAuthenticationToken> getAuthToken(HttpServletRequest request) throws JwtException, IllegalArgumentException {
        String token = request.getHeader(securityProperties.getAuthHeader());
        if (token == null || token.isEmpty() || !token.startsWith(securityProperties.getAuthTokenPrefix())) {
            return Optional.empty();
        } else if (!token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token must start with 'Bearer'");
        } else {
            String username = getUsername(token);
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Token contains no user");
            } else {
                Collection<? extends GrantedAuthority> authorities = userRepository.findByName(username)
                    .map(ApplicationUser::getAuthorities)
                    .orElseThrow(() -> new NotFoundException(String.format("Could not find user with name '%s'", username)));

                return Optional.of(new UsernamePasswordAuthenticationToken(username, null, authorities));
            }
        }
    }

    private String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(securityProperties.getJwtSecret().getBytes())
            .build()
            .parseClaimsJws(token.replace(securityProperties.getAuthTokenPrefix(), ""))
            .getBody()
            .getSubject();
    }
}
