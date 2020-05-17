package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CalendarMapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TestCalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Slf4j
@RestController
@RequestMapping(value = CalendarEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CalendarEndpoint {
    static final String BASE_URL = "/calendars";
    private final CalendarService calendarService;
    private final EventService eventService;
    private final OrganisationService organisationService;
    private final CalendarMapper calendarMapper;
    private final EventMapper eventMapper;
    private final TestCalendarMapper testMapper;


   /** @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto editCalendar(@RequestBody CalendarDto calendar) {
        log.info("PUT " + BASE_URL + "/{}", calendar);
        try {
            Calendar calendarEntity = calendarMapper.calendarDtoToCalendar(calendar);
            return calendarMapper.calendarToCalendarDto(calendarService.update(calendarEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }**/

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public CalendarDto getById(@PathVariable("id") int id) {
        log.info("GET " + BASE_URL + "/{}", id);
        try {


            return calendarMapper.calendarToCalendarDto(calendarService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/all")
    public List<CalendarDto> getAll() {
        log.info("GET All" + BASE_URL + "/all");
        try {

            return testMapper.calendarsToCalendarDtos(calendarService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/thisWeek")
    @ApiOperation(value = "Get events of this week")
    public List<EventDto> getEventsOfTheWeek(@RequestParam(value = "id")  String id,
                                             @RequestParam(value = "from") String start,
                                             @RequestParam(value = "to") String end){
        log.info("GET" + BASE_URL + "Get events of this week {}", id);
        try {
            String[] start1 = start.split(" GMT");
            String[] end1 = end.split(" GMT");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss", Locale.US);
            LocalDateTime dateTimeStart = LocalDateTime.parse(start1[0], formatter);
            LocalDateTime dateTimeEnd = LocalDateTime.parse(end1[0], formatter);
            List<Event> events = eventService.findForDates(dateTimeStart, dateTimeEnd);
            List<EventDto> eventDtos = new ArrayList<>();
            events.removeIf(e -> e.getCalendar().getId() != Integer.parseInt(id));
            events.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
        catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search")
    @ApiOperation(value = "Get calendars with name")
    public List<CalendarDto> searchCalendarCombo(@RequestParam(value = "name")  String name){
        log.info("GET"+BASE_URL+"search {}", name);
        try{
        List<Calendar> fromCalendars = calendarService.findByName(name);
        List<Organisation> fromOrganisations = organisationService.findByName(name);
        List<Event> fromEvents=eventService.findByName(name);
            for (Organisation o: fromOrganisations) {
                if(!o.getCalendars().isEmpty()){
                fromCalendars.addAll(o.getCalendars());}
            }
            for (Event e: fromEvents) {
                fromCalendars.add(e.getCalendar());
            }
            List<CalendarDto> calendarDtos = new ArrayList<>();
            fromCalendars.forEach(calendar -> calendarDtos.add(calendarMapper.calendarToCalendarDto(calendar)));
            if(calendarDtos.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing was found");
            }
            return calendarDtos;
        }
        catch (ServiceException e){
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

            Calendar calendarEntity = calendarMapper.calendarDtoToCalendar(calendar);

            /**Note: If you set values as eventIds, post method will return them, BUT it will not store them in the DB.
            If you call getById afterwards its empty as it should be because Create Calendar is NOT for inserting events.
            Events are only inserted in calendars by creating a new event and setting the right calendar_id.
             It is best to not give the option to set eventIds while creating a calendar in the frontend at all.
            **/
            return calendarMapper.calendarToCalendarDto(calendarService.save(calendarEntity));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


}
