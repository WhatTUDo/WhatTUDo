package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CollisionResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CollisionResponseMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import at.ac.tuwien.sepm.groupphase.backend.service.EventCollisionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = EventCollisionEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventCollisionEndpoint {
    static final String BASE_URL = "/collision";

    private final EventCollisionService eventCollisionService;
    private final EventMapper eventMapper;
    private final CollisionResponseMapper collisionResponseMapper;


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Get Event Collisions and Date Recommendations", authorizations = {@Authorization(value = "apiKey")})
    public CollisionResponseDto getEventCollisions(@RequestBody EventDto eventDto) {
        Integer threshold = 3;
        Long timespan = 12L;
        log.info("Get Event Collisions with set CS threshold {} and additional timespan {}h", threshold, timespan);
        try {
            List<EventCollision> eventCollisions = this.eventCollisionService.getEventCollisions(eventMapper.eventDtoToEvent(eventDto), threshold, timespan);
            if (!eventCollisions.isEmpty()) {
                EventCollision min = eventCollisions.stream().min(Comparator.comparing(e -> e.getCollisionScore())).get();
                List<LocalDateTime[]> suggestions = this.eventCollisionService.getAlternativeDateSuggestions(eventMapper.eventDtoToEvent(eventDto), min.getCollisionScore());
                if(suggestions.size()>10){
                List<LocalDateTime[]> ten_suggestions= suggestions.subList(0, 9);
                return collisionResponseMapper.mapCollisionResponseDto(eventCollisions, ten_suggestions);
                }
                return collisionResponseMapper.mapCollisionResponseDto(eventCollisions, suggestions);
            }
            return collisionResponseMapper.mapCollisionResponseDto(eventCollisions, new ArrayList<>());

        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
