package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CollisionResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = EventCollisionEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventCollisionEndpoint {
    static final String BASE_URL = "/collision";

    private final EventCollisionService eventCollisionService;
    private final EventMapper eventMapper;


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Get Event Collisions and Date Recommendations", authorizations = {@Authorization(value = "apiKey")})
    public CollisionResponseDto getEventCollisions(@RequestBody EventDto eventDto) {
        log.info("Get Event Collisions");
        //TODO: this.
        try {
            List<EventCollision> eventCollisions = this.eventCollisionService.getEventCollisions(eventMapper.eventDtoToEvent(eventDto), 3, 12L);
            return null;
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
