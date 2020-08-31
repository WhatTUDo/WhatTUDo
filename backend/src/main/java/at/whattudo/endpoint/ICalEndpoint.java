package at.whattudo.endpoint;

import at.whattudo.auth.authorities.ICalAuthority;
import at.whattudo.auth.jwt.JwtTokenizer;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.service.ICalService;
import at.whattudo.service.UserService;
import biweekly.Biweekly;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping(value = ICalEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ICalEndpoint {
    static final String BASE_URL = "/ical";
    private final ICalService iCalService;
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/all-calendars.ics", produces = "text/calendar")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get all calendars")
    @ResponseBody
    public void getCalendars(OutputStream out) {
        try {
            Biweekly.write(iCalService.getAllCalendars()).go(out);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/user", produces = "text/plain")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get token to access the user calendar with")
    public @ResponseBody String getToken(@AuthenticationPrincipal String username) {
        try {
            return jwtTokenizer.getAuthToken(username, Collections.singleton(ICalAuthority.ICAL.getAuthority()));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()") // This endpoint does not use our normal authenticator, because the token will be passed in the URL instead
    @GetMapping(value = "/{token}/user.ics", produces = "text/calendar")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get calendars for user")
    public void getCalendar(@PathVariable String token, OutputStream out) {
        try {
            String username = jwtTokenizer.getUsernameForIcal(token);
            ApplicationUser user = userService.getUserByName(username);
            Biweekly.write(iCalService.getCalendarsForUser(user)).go(out);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}/calendar.ics", produces = "text/calendar")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get single calendar")
    @ResponseBody
    public void getCalendar(@PathVariable Integer id, OutputStream out) {
        try {
            Biweekly.write(iCalService.getCalendar(id)).go(out);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
