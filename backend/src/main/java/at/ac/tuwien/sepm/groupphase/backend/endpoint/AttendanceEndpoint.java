package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.StatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = AttendanceEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AttendanceEndpoint {
    static final String BASE_URL = "/attendance";
    private final AttendanceService attendanceService;
    private final StatusMapper statusMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('SYSADMIN') || #dto.username == authentication.name")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create attendance", authorizations = {@Authorization(value = "apiKey")})
    public StatusDto create(@RequestBody StatusDto dto) {
        log.info("create status");
        try {
            return statusMapper.applicationStatusToStatusDto(attendanceService.create(statusMapper.statusDtoToApplicationStatus(dto)));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getAttendees/{id}")
    @ApiOperation(value = "get attendance", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getUsersAttendingEvent(@PathVariable(value = "id") Integer eventId) {
        log.info("get attendees of event with id {}", eventId);
        try {
            return userMapper.applicationUserToUserDtoList(attendanceService.getUsersAttendingEvent(eventId));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getInterested/{id}")
    @ApiOperation(value = "get interested", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getUsersInterestedInEvent(@PathVariable(value = "id") Integer eventId) {
        log.info("get interested users of event with id {}", eventId);
        try {
            return userMapper.applicationUserToUserDtoList(attendanceService.getUsersInterestedInEvent(eventId));

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getDeclined/{id}")
    @ApiOperation(value = "get declined", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getUsersDecliningEvent(@PathVariable(value = "id") Integer eventId) {
        log.info("get users who declined event with id {}", eventId);
        try {
            return userMapper.applicationUserToUserDtoList(attendanceService.getUsersDecliningEvent(eventId));

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/userAttending/{id}")
    public List<EventDto> getEventsUserIsAttending(@PathVariable(value = "id") Integer userId) {
        try {
            List<Event> events = attendanceService.getEventUserIsAttending(userId);
            List<EventDto> eventDtos = new ArrayList<>();
            events.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/userInterested/{id}")
    public List<EventDto> getEventsUserIsInterested(@PathVariable(value = "id") Integer userId) {
        try {
            List<Event> events = attendanceService.getEventUserIsInterested(userId);
            List<EventDto> eventDtos = new ArrayList<>();
            events.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
