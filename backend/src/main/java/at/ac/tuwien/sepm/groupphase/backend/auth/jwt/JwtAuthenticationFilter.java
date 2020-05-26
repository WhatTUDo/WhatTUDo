package at.ac.tuwien.sepm.groupphase.backend.auth.jwt;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.lang.invoke.MethodHandles;
import java.util.Collection;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties,
                                   JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    @Transactional
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginDto user = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            //Compares the user with CustomUserDetailService#loadUserByUsername and check if the credentials are correct
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword())
            );
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
        LOGGER.debug("Invalid authentication attempt: {}", failed.getMessage());
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

        Collection<String> authorities = user.getAuthorityStrings();
        response.getWriter().write(jwtTokenizer.getAuthToken(user.getUsername(), authorities));
        LOGGER.info("Successfully authenticated user {}", user.getUsername());
    }
}
