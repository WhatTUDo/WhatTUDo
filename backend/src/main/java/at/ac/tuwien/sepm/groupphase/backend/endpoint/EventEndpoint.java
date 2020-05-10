package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = EventEndpoint.BASE_URL)
public class EventEndpoint {
    static final String BASE_URL = "/events";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;


    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

  //  @PreAuthorize("hasRole('Member')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete event", authorizations = {@Authorization(value = "apiKey")})
    public void deleteEvent(@RequestBody EventDto eventDto){
        LOGGER.info("DELETE /api/v1/events body: {}", eventDto);
        try {
            eventService.delete(eventMapper.dtoToEntity(eventDto));
        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }catch (ValidationException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }

    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto post(@RequestBody EventDto event){
        LOGGER.info("POST " + BASE_URL + "/{}", event);
        try {
            Event eventEntity = eventMapper.dtoToEntity(event);
            return eventMapper.entityToDto(eventService.save(eventEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable("id") int id) {
        LOGGER.info("GET " + BASE_URL + "/{}", id);
        try {
            return eventMapper.entityToDto(eventService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
