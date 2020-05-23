package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = EventEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventEndpoint {
    static final String BASE_URL = "/events";
    private final EventService eventService;
    private final EventMapper eventMapper;


    @PreAuthorize("hasPermission(#eventDto, 'MEMBER')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @ApiOperation(value = "Delete event", authorizations = {@Authorization(value = "apiKey")})
    public void deleteEvent(@RequestBody EventDto eventDto) {
        log.info("DELETE /api/v1/events body: {}", eventDto);
        try {
            eventService.delete(eventMapper.eventDtoToEvent(eventDto));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#event, 'MEMBER')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto post(@RequestBody EventDto event) {
        log.info("POST " + BASE_URL + "/{}", event);
        try {
            Event eventEntity = eventMapper.eventDtoToEvent(event);
            return eventMapper.eventToEventDto(eventService.save(eventEntity));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get Multiple Events")
    public List<EventDto> getMultiple(@RequestParam("name") @Nullable String name,
                                      @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                      @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("GET" + BASE_URL + "?name={}&from={}&to={}&calendar_id", name, start, end);
        try {
            List<Event> events = eventService.findForDates(start, end);
            List<EventDto> eventDtos = new ArrayList<>();

            events.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable("id") int id) {
        log.info("GET " + BASE_URL + "/{}", id);
        try {
            return eventMapper.eventToEventDto(eventService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PreAuthorize("hasPermission(#eventDto, 'MEMBER')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto editEvent(@RequestBody EventDto eventDto) {
        log.info("PUT " + BASE_URL + "/{}", eventDto);
        try {
            Event eventEntity = eventMapper.eventDtoToEvent(eventDto);
            return eventMapper.eventToEventDto(eventService.update(eventEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

}
