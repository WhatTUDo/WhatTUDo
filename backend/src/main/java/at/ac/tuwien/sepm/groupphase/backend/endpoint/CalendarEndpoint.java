package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TestCalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = CalendarEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CalendarEndpoint {
    static final String BASE_URL = "/calendars";
    private final CalendarService calendarService;
    private final EventService eventService;
    private final OrganizationService organizationService;
    private final CalendarMapper calendarMapper;
    private final TestCalendarMapper testMapper;

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public CalendarDto getById(@PathVariable("id") int id) {
        try {
            return calendarMapper.calendarToCalendarDto(calendarService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/all")
    public List<CalendarDto> getAll() {
        try {
            return calendarMapper.calendarsToCalendarDtos(calendarService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search")
    @ApiOperation(value = "Get calendars with name")
    public List<CalendarDto> searchCalendarCombo(@RequestParam(value = "name") String name) {
        try {
            List<Calendar> fromCalendars = calendarService.findByName(name);
            List<Organization> fromOrganizations = organizationService.findByName(name);
            List<Event> fromEvents = eventService.findByName(name);
            for (Organization o : fromOrganizations) {
                if (!o.getCalendars().isEmpty()) {
                    fromCalendars.addAll(o.getCalendars());
                }
            }
            for (Event e : fromEvents) {
                fromCalendars.add(e.getCalendar());
            }
            List<CalendarDto> calendarDtos = new ArrayList<>();
            fromCalendars.forEach(calendar -> calendarDtos.add(calendarMapper.calendarToCalendarDto(calendar)));
            if (calendarDtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.OK, "Nothing was found"); //FIXME return empty array?
            }
            return calendarDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }


    }




   // @PreAuthorize("hasPermission(#calendar, 'MOD')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto create(@RequestBody CalendarDto calendar) {
        try {
            List<Integer> eventsShouldBeEmpty = new ArrayList<>();
            calendar.setEventIds(eventsShouldBeEmpty);
            Calendar calendarEntity = testMapper.calendarDtoToCalendar(calendar);
            return testMapper.calendarToCalendarDto(calendarService.save(calendarEntity));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'CAL', 'MOD')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete calendar", authorizations = {@Authorization(value = "apiKey")})
    public void deleteCalendar(@PathVariable("id") Integer id) {
        try {
            calendarService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#calendarDto, 'MOD')")
    @CrossOrigin
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto editCalendar(@RequestBody CalendarDto calendarDto) {
        try {
            Calendar calendar = testMapper.calendarDtoToCalendar(calendarDto);
            return testMapper.calendarToCalendarDto(calendarService.update(calendar));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
