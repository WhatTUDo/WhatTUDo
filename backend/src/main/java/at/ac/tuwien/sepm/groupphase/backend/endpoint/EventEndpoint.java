package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1/events")
public class EventEndpoint {
    static final String BASE_URL = "/events";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private EventService eventService;
    private EventMapper eventMapper;


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
        eventService.delete(eventMapper.dtoToEntity(eventDto));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto post(@RequestBody EventDto event){
        LOGGER.info("POST /api/v1/events body: {}", event);
        //TODO: try catch, Logger
        Event eventEntity = eventMapper.dtoToEntity(event);
        return eventMapper.entityToDto(eventService.save(eventEntity));

    }

    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable("id") int id) {
        LOGGER.info("GET /api/v1/events body: {}", id);
        return eventMapper.entityToDto(eventService.findById(id));
    }


}
