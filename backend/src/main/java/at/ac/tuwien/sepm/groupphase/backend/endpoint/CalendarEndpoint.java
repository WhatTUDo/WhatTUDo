package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganisationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TestCalendarMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
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

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = CalendarEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CalendarEndpoint {
    static final String BASE_URL = "/calendars";
    private final CalendarService calendarService;
    private final CalendarMapper calendarMapper;

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

            return testMapper.calendarToCalendarDto(calendarService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/")
    public List<CalendarDto> getAll() {
        log.info("GET All" + BASE_URL + "");
        try {

            return testMapper.calendarsToCalendarDtos(calendarService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto post(@RequestBody CalendarDto calendar) {
        log.info("POST " + BASE_URL + "/{}", calendar);
        try {

            Calendar calendarEntity = testMapper.calendarDtoToCalendar(calendar);

            /**Note: If you set values as eventIds, post method will return them, BUT it will not store them in the DB.
            If you call getById afterwards its empty as it should be because Create Calendar is NOT for inserting events.
            Events are only inserted in calendars by creating a new event and setting the right calendar_id.
             It is best to not give the option to set eventIds while creating a calendar in the frontend at all.
            **/
            return testMapper.calendarToCalendarDto(calendarService.save(calendarEntity));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


}
