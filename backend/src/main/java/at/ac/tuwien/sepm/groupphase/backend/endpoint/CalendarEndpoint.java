package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CalendarMapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;


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
    private final EventMapper eventMapper;


    private final TestCalendarMapper testMapper;


    @CrossOrigin
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get Calendar by ID")
    public CalendarDto getById(@PathVariable("id") int id) {
        log.info("GET " + BASE_URL + "/{}", id);
        try {
            return calendarMapper.calendarToCalendarDto(calendarService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @GetMapping(value = "")
    @ApiOperation(value = "Get all Calendars")
    public List<CalendarDto> getAll() {
        log.info("GET all" + BASE_URL + "");
        try {
            return testMapper.calendarsToCalendarDtos(calendarService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search")
    @ApiOperation(value = "Get calendars with name")
    public List<CalendarDto> searchCalendarCombo(@RequestParam(value = "name")  String name) {
        log.info("GET" + BASE_URL + "search {}", name);
        try {
            List<Calendar> fromCalendars = calendarService.findByName(name);
            List<Organization> fromOrganisations = organizationService.findByName(name);
            List<Event> fromEvents = eventService.findByName(name);
            Set<Calendar> calendars = new HashSet<>();
            calendars.addAll(fromCalendars);
            for (Organization o : fromOrganisations) {
                if (!o.getCalendars().isEmpty()) {
                    calendars.addAll(o.getCalendars());
                }
            }
                for (Event e : fromEvents) {
                    calendars.add(e.getCalendar());
                }
                List<CalendarDto> calendarDtos = new ArrayList<>();
                calendars.forEach(calendar -> calendarDtos.add(calendarMapper.calendarToCalendarDto(calendar)));
                if (calendarDtos.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing was found");
                }
                return calendarDtos;

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }

    }


    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto post(@RequestBody CalendarDto calendar) {
        log.info("POST " + BASE_URL + "/{}", calendar);
        try {


            /**Note: This is to make sure that post will not return any wrongly eventList inputs (which won't get store in the
             * DB anyway) under any circumstances. (Otherwise it would. but it won't save them in the DB in both cases)
             * Create Calendar is NOT for inserting events.
             Events are only inserted in calendars by creating a new event and setting the right calendar_id.
             It is best to not give the option to set eventIds while creating a calendar in the frontend at all.
            **/

            List<Integer> eventsShouldBeEmpty = new ArrayList<Integer>();
            calendar.setEventIds(eventsShouldBeEmpty);

            Calendar calendarEntity = testMapper.calendarDtoToCalendar(calendar);

            return testMapper.calendarToCalendarDto(calendarService.save(calendarEntity));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


}
