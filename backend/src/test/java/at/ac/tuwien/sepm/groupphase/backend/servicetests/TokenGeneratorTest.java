package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.auth.authorities.ICalAuthority;
import at.ac.tuwien.sepm.groupphase.backend.auth.jwt.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.util.ICalMapper;
import biweekly.component.VEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class TokenGeneratorTest {
    @Autowired
    JwtTokenizer jwtTokenizer;

    @Test
    public void can_use_auth_token_for_auth() {
        String token = jwtTokenizer.getAuthToken("user", Collections.emptyList());
        String tokenUser = jwtTokenizer.getUsernameForAuth(token);
        assertEquals("user", tokenUser);
    }

    @Test
    public void cant_use_auth_token_for_ical() {
        String token = jwtTokenizer.getAuthToken("user", Collections.emptyList());
        assertThrows(AccessDeniedException.class, () -> jwtTokenizer.getUsernameForIcal(token));
    }

    @Test
    public void can_use_ical_token_for_ical() {
        String token = jwtTokenizer.getAuthToken("user", Collections.singletonList(ICalAuthority.ICAL.getAuthority()));
        String tokenUser = jwtTokenizer.getUsernameForIcal(token);
        assertEquals("user", tokenUser);
    }

    @Test
    public void cant_use_ical_token_for_auth() {
        String token = jwtTokenizer.getAuthToken("user", Collections.singletonList(ICalAuthority.ICAL.getAuthority()));
        assertThrows(AccessDeniedException.class, () -> jwtTokenizer.getUsernameForAuth(token));
    }

    @Test
    public void prefixed_token_working() {
        String token = jwtTokenizer.getAuthTokenPrefixed("user", Collections.emptyList());
        String tokenUser = jwtTokenizer.getUsernameForAuth(token);
        assertEquals("user", tokenUser);
    }
}
