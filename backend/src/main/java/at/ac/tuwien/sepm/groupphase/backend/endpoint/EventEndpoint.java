package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

//TODO: annotations

@RestController
@RequestMapping(EventEndpoint.BASE_URL)
public class EventEndpoint {

    static final String BASE_URL = "/events";
    private final EventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto post(@RequestBody EventDto event){

        //TODO: try catch, Logger
        Event eventEntity = eventMapper.dtoToEntity(event);
        return eventMapper.entityToDto(eventService.save(eventEntity));

    }

    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable("id") int id) {
        //TODO: looger, try catch
        return eventMapper.entityToDto(eventService.findById(id));
    }


}
