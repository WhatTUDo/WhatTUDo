package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarCreateDto;
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
import com.google.common.io.ByteStreams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Slf4j
@RestController
@RequestMapping(value = CalendarEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CalendarEndpoint {
    public static final String BASE_URL = "/calendars";
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/all")
    public List<CalendarDto> getAll() {
        try {
            return calendarMapper.calendarsToCalendarDtos(calendarService.findAll());
        } catch (NotFoundException e) {
            return Collections.emptyList();
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search")
    @ApiOperation(value = "Get calendars with name")
    public List<CalendarDto> searchCalendarCombo(@RequestParam(value = "name") String name) {
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



    @PreAuthorize("hasPermission(#dto, 'MOD')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto create(@RequestBody CalendarCreateDto dto) {
        try {
            Calendar calendarEntity = calendarMapper.calendarCreateDtoToCalendar(dto);
            return calendarMapper.calendarToCalendarDto(calendarService.save(calendarEntity));
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#dto, 'MOD')")
    @CrossOrigin
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit calendar", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto editCalendar(@RequestBody CalendarDto dto) {
        try {
            Calendar calendar = testMapper.calendarDtoToCalendar(dto);
            return testMapper.calendarToCalendarDto(calendarService.update(calendar));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#dto, 'MOD')")
    @CrossOrigin
    @PutMapping(value = "/organizations")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Associated Organizations of a given Calendar!")
    public CalendarDto updateOrganizationsForCalendar(@RequestBody CalendarDto dto) {
        try {
            Calendar calendar = calendarMapper.calendarDtoToCalendar(dto);
            List<Organization> updatedOrganizationList = calendar.getOrganizations();
            return calendarMapper.calendarToCalendarDto(calendarService.updateOrganizationsWithList(calendar, updatedOrganizationList));

        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get cover image", authorizations = {@Authorization(value = "apiKey")})
    public @ResponseBody byte[] getCoverImage(@PathVariable int id) {
        try {
            Byte[] coverImageBlob = calendarService.findById(id).getCoverImage();
            byte[] byteArray;
            if (coverImageBlob != null) {
                byteArray = new byte[coverImageBlob.length];
                for (int i = 0; i < coverImageBlob.length; i++) byteArray[i] = coverImageBlob[i];
            } else {
                try {
                    InputStream stream = CalendarEndpoint.class.getResourceAsStream("/images/default-calendar-background.jpg");
                    //noinspection UnstableApiUsage
                    byteArray = ByteStreams.toByteArray(stream);
                } catch (IOException e) {
                    byteArray = new byte[0];
                }
            }
            return byteArray;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'CAL', 'MOD')")
    @CrossOrigin
    @PostMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Set cover image", authorizations = {@Authorization(value = "apiKey")})
    public CalendarDto setCoverImage(@PathVariable int id, @RequestParam("imagefile") MultipartFile file) {
        try {
            Calendar calendar = calendarService.setCoverImage(calendarService.findById(id), file.getBytes());
            return calendarMapper.calendarToCalendarDto(calendar);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), e);
        }
    }
}
