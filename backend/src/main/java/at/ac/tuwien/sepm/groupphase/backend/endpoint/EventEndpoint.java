package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LabelMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = EventEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventEndpoint {
    static final String BASE_URL = "/events";
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final LabelService labelService;
    private final LabelMapper labelMapper;


    //@PreAuthorize("hasPermission(#eventDto, 'MEMBER')")
    @CrossOrigin
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @ApiOperation(value = "Delete event", authorizations = {@Authorization(value = "apiKey")})
    public void deleteEvent(@RequestBody EventDto eventDto) {
        try {
            eventService.delete(eventMapper.eventDtoToEvent(eventDto));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e);
        }
    }

   // @PreAuthorize("hasPermission(#event, 'MEMBER')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto post(@RequestBody EventDto event) {
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
    public List<EventDto> getMultiple(
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
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
            return new ArrayList<>();
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable("id") int id) {
        try {
            return eventMapper.eventToEventDto(eventService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
        }
    }

    @CrossOrigin
    @PreAuthorize("hasPermission(#eventDto, 'MEMBER')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto editEvent(@RequestBody EventDto eventDto) {
        try {
            Event eventEntity = eventMapper.eventDtoToEvent(eventDto);
            return eventMapper.eventToEventDto(eventService.update(eventEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/calendarId/{id}")
    @ApiOperation(value = "Get Calendar Events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDto> getEventsByCalendarId(@PathVariable("id") Integer id) {
        log.info("getEventsByCalendarId");
        try {
            List<Event> events = eventService.getByCalendarId(id);
            List<EventDto> eventDtos = new ArrayList<>();
            events.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    //@PreAuthorize("hasPermission(#eventDto, 'MOD')")
    @Transactional
    @DeleteMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Labels from an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto removeLabelsFromEvent(@PathVariable(value = "id") Integer eventId, @RequestParam(value = "labelId") List<Integer> labelIds) {
        try {
            Collection<Label> labels = labelIds.stream().map(labelService::findById).collect(Collectors.toList());
            Event event = eventService.removeLabels(eventService.findById(eventId), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    //@PreAuthorize("hasPermission(#eventDto, 'MOD')")
  /**  @Transactional
    @PutMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Labels to an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto addLabelToEvent(@PathVariable(value = "id") Integer eventId, @RequestParam(value = "labelId") List<Integer> labelIds) {
        try {
            Collection<Label> labels = labelIds.stream().map(labelService::findById).collect(Collectors.toList());
            Event event = eventService.addLabels(eventService.findById(eventId), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }**/

    //@PreAuthorize("hasPermission(#eventDto, 'MOD')")
    @Transactional
    @PutMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Labels of an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto updateLabelsOfEvent(@PathVariable(value = "id") Integer eventId, @RequestBody List<Label> labels) {
        try {

            Event event = eventService.updateLabels(eventService.findById(eventId), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }





    @Transactional
    @GetMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Labels by Id", authorizations = {@Authorization(value = "apiKey")})
    public List<LabelDto> getLabelsById(@PathVariable(value = "id") int id) {
        try {

            List<LabelDto> results = new ArrayList<>();

            (labelService.findByEventId(id)).forEach(it -> results.add(labelMapper.labelToLabelDto(it)));


            return results;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
