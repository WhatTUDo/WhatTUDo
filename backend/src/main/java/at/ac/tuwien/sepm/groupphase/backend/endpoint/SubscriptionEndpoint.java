package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SubscriptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SubscriptionMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = SubscriptionEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubscriptionEndpoint {
    static final String BASE_URL = "/subscription";

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final CalendarService calendarService;
    private final SubscriptionMapper subscriptionMapper;
    private final CalendarMapper calendarMapper;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('SYSADMIN') || #subscriptionDto.userName == authentication.name")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @ApiOperation(value = "Create a new Subscription", authorizations = {@Authorization(value = "apiKey")})
    public SubscriptionDto create(@RequestBody SubscriptionDto subscriptionDto) {
        try {
            return subscriptionMapper.subscriptionToSubscriptionDto(subscriptionService.create(subscriptionMapper.subscriptionDtoToSubscription(subscriptionDto)));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('SYSADMIN') || #subscriptionDto.userName == authentication.name")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @ApiOperation(value = "Delete an existing Subscription", authorizations = {@Authorization(value = "apiKey")})
    public SubscriptionDto delete(@RequestBody SubscriptionDto subscriptionDto) {
        try {
            return subscriptionMapper.subscriptionToSubscriptionDto(subscriptionService.delete(subscriptionMapper.subscriptionDtoToSubscription(subscriptionDto)));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/calendars/{id}")
    @ApiOperation(value = "Get all Calendars to which a User is subscribed", authorizations = {@Authorization(value = "apiKey")})
    public List<CalendarDto> getAllSubscribedCalenders(@PathVariable(value = "id") Integer userId) {
        try {
            ApplicationUser user = userService.findUserById(userId);
            return calendarMapper.calendarsToCalendarDtos(subscriptionService.getSubsribedCalendarsForUser(user));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    @ApiOperation(value = "Get all Users who subscribe to a Calendar", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getAllSubscribedUsers(@PathVariable(value = "id") Integer calendarId) {
        try {
            Calendar calendar = calendarService.findById(calendarId);
            return userMapper.applicationUserToUserDtoList(subscriptionService.getSubscribedUsersForCalendar(calendar));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
