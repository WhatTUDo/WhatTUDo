package at.whattudo.auth.jwt;

import at.whattudo.auth.authorities.ICalAuthority;
import at.whattudo.config.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtTokenizer {
    private final SecurityProperties securityProperties;

    public String getAuthTokenPrefixed(String user, Collection<String> roles) {
        return securityProperties.getAuthTokenPrefix() + getAuthToken(user, roles);
    }

    public String getAuthToken(String user, Collection<String> roles) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getJwtType())
            .setIssuer(securityProperties.getJwtIssuer())
            .setAudience(securityProperties.getJwtAudience())
            .setSubject(user)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
            .claim("rol", roles)
            .compact();
    }

    public String getUsernameForAuth(String token) {
        Claims claims = getClaims(token);
        if (!claims.get("rol", Collection.class).contains(ICalAuthority.ICAL.getAuthority())) {
            return claims.getSubject();
        } else {
            throw new AccessDeniedException("Using token for iCal access to authenticate");
        }
    }

    public String getUsernameForIcal(String token) {
        Claims claims = getClaims(token);
        //noinspection rawtypes
        Collection roles = claims.get("rol", Collection.class);
        if (roles.size() == 1 && roles.contains(ICalAuthority.ICAL.getAuthority())) {
            return claims.getSubject();
        } else {
            throw new AccessDeniedException("Using token for authentication to access iCal");
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(securityProperties.getJwtSecret().getBytes())
            .build()
            .parseClaimsJws(token.replace(securityProperties.getAuthTokenPrefix(), ""))
            .getBody();
    }
}
