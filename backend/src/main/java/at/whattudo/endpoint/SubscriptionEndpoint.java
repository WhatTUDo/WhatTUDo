package at.whattudo.endpoint;


import at.whattudo.endpoint.dto.CalendarDto;
import at.whattudo.endpoint.dto.LoggedInUserDto;
import at.whattudo.endpoint.dto.SubscriptionDto;
import at.whattudo.endpoint.mapper.CalendarMapper;
import at.whattudo.endpoint.mapper.SubscriptionMapper;
import at.whattudo.endpoint.mapper.UserMapper;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Subscription;
import at.whattudo.exception.NotFoundException;
import at.whattudo.service.CalendarService;
import at.whattudo.service.SubscriptionService;
import at.whattudo.service.UserService;
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
            Subscription subscription = subscriptionMapper.subscriptionDtoToSubscription(subscriptionDto);
            Subscription savedSubscription = subscriptionService.create(subscription);
            return subscriptionMapper.subscriptionToSubscriptionDto(savedSubscription);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an existing Subscription", authorizations = {@Authorization(value = "apiKey")})
    public SubscriptionDto delete(@PathVariable(value = "id") Integer subscriptionId) {
        try {
            Subscription subscription = subscriptionService.getById(subscriptionId);
            return subscriptionMapper.subscriptionToSubscriptionDto(subscriptionService.delete(subscription));
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

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/for/calendar/{calendarId}")
    @ApiOperation(value = "Get Subscriptions for a Calendar", authorizations = {@Authorization(value = "apiKey")})
    public List<SubscriptionDto> getSubscriptionsForCalendar(@PathVariable("calendarId") Integer calendarId) {
        try {
            Calendar calendar = calendarService.findById(calendarId);
            return subscriptionMapper.subscriptionDtoList(subscriptionService.getSubscriptionsForCalendar(calendar));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/for/user/{userId}")
    @ApiOperation(value = "Get Subscriptions by a User", authorizations = {@Authorization(value = "apiKey")})
    public List<SubscriptionDto> getSubscriptionsForuser(@PathVariable("userId") Integer userId) {
        try {
            ApplicationUser user = userService.findUserById(userId);
            return subscriptionMapper.subscriptionDtoList(subscriptionService.getSubscriptionsByUser(user));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
