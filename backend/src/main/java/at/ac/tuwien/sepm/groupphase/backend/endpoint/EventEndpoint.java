package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommentMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LabelMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CommentService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
    public static final String BASE_URL = "/events";
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final LabelService labelService;
    private final LabelMapper labelMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PreAuthorize("hasPermission(#id, 'EVENT', 'MOD')")
    @CrossOrigin
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete event", authorizations = {@Authorization(value = "apiKey")})
    public void deleteEvent(@PathVariable Integer id) {
        try {
            eventService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PreAuthorize("hasPermission(#dto, 'MEMBER')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto post(@RequestBody EventDto dto) {
        try {
            Event eventEntity = eventMapper.eventDtoToEvent(dto);
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

    @PreAuthorize("hasPermission(#dto, 'MEMBER')")
    @CrossOrigin
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto editEvent(@RequestBody EventDto dto) {
        try {
            Event eventEntity = eventMapper.eventDtoToEvent(dto);
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


    @PreAuthorize("hasPermission(#id, 'EVENT', 'MOD')")
    @Transactional
    @DeleteMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Labels from an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto removeLabelsFromEvent(@PathVariable(value = "id") Integer id, @RequestParam(value = "labelId") List<Integer> labelIds) {
        try {
            Collection<Label> labels = labelIds.stream().map(labelService::findById).collect(Collectors.toList());
            Event event = eventService.removeLabels(eventService.findById(id), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'EVENT', 'MOD')")
    @Transactional
    @PostMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Labels to an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto addLabelToEvent(@PathVariable(value = "id") Integer id, @RequestParam(value = "labelId") List<Integer> labelIds) {
        try {
            Collection<Label> labels = labelIds.stream().map(labelService::findById).collect(Collectors.toList());
            Event event = eventService.addLabels(eventService.findById(id), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'EVENT', 'MOD')")
    @Transactional
    @PutMapping(value = "/{id}/labels")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Labels of an Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto updateLabelsOfEvent(@PathVariable(value = "id") Integer id, @RequestBody List<LabelDto> labelsDto) {
        try {

            List<Label> labels = new ArrayList<Label>();
            for (LabelDto l : labelsDto) {

                labels.add(labelMapper.labelDtoToLabel(l));

            }

            Event event = eventService.updateLabels(eventService.findById(id), labels);
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PreAuthorize("permitAll()")
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

    @PreAuthorize("permitAll()")
    @Transactional
    @GetMapping(value = "/{id}/comments")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Comments by Event Id", authorizations = {@Authorization(value = "apiKey")})
    public List<CommentDto> getCommentsByEventId(@PathVariable(value = "id") int id) {
        try {

            List<CommentDto> results = new ArrayList<>();

            (commentService.findByEventId(id)).forEach(it -> results.add(commentMapper.commentToCommentDto(it)));


            return results;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit event", authorizations = {@Authorization(value = "apiKey")})
    public @ResponseBody byte[] getCoverImage(@PathVariable int id) {
        try {
            Byte[] coverImageBlob = eventService.findById(id).getCoverImage();
            byte[] byteArray = new byte[coverImageBlob.length];
            for (int i = 0; i < coverImageBlob.length; i++) byteArray[i] = coverImageBlob[i];
            return byteArray;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'EVENT', 'MOD')")
    @CrossOrigin
    @PostMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto setCoverImage(@PathVariable int id, @RequestParam("imagefile") MultipartFile file) {
        try {
            Event event = eventService.setCoverImage(eventService.findById(id), file.getBytes());
            return eventMapper.eventToEventDto(event);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), e);
        }
    }
}
