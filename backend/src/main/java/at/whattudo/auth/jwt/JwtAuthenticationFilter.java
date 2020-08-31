package at.whattudo.auth.jwt;

import at.whattudo.config.properties.SecurityProperties;
import at.whattudo.endpoint.dto.UserLoginDto;
import at.whattudo.entity.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    @Transactional
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        try {
            log.info("Attempting Authentication");
            UserLoginDto user = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            //Compares the user with CustomUserDetailService#loadUserByUsername and checks if the credentials are correct
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
            ));
        } catch (IOException e) {
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        }
    }

    @Override
    @Transactional
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        log.debug("Invalid authentication attempt: {}", failed.getMessage());
    }


    @Override
    @Transactional
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException {
        ApplicationUser user = ((ApplicationUser) authResult.getPrincipal());
        response.getWriter().write(jwtTokenizer.getAuthTokenPrefixed(user.getUsername(), user.getAuthorityStrings()));
        log.info("Successfully authenticated user {} for auhtorities {}", user.getUsername(), user.getAuthorityStrings());
    }
}
