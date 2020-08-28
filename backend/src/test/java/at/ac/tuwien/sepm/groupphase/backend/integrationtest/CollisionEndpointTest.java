package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventCollisionEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CollisionResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCollisionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.Collections;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class CollisionEndpointTest {
    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    EventCollisionEndpoint eventCollisionEndpoint;



    @WithMockUser
    @Test
    public void getEventCollisionsAndSuggestions() {
        Organization orga = new Organization("Test Organization");
        Calendar calendar =  calendarRepository.save(new Calendar("Calendar test", Collections.singletonList(orga)));
        Location location =  locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));

        eventRepository.save(new Event("Event colliding 1", LocalDateTime.of(2021, 8, 8, 10, 0),
            LocalDateTime.of(2021, 8, 8, 11, 0), calendar));
        eventRepository.save(new Event("Event colliding 2", LocalDateTime.of(2021, 8, 8, 9, 45),
            LocalDateTime.of(2021, 8, 8, 11, 0), calendar));

        CollisionResponseDto eventCollisionDto = eventCollisionEndpoint.getEventCollisions(new EventDto(1, "Event saving", LocalDateTime.of(2021, 8, 8, 10, 0),
            LocalDateTime.of(2021, 8, 8, 11, 0), calendar.getId(), location.getId()));

        assert (eventCollisionDto.getEventCollisions() != null);
        assert (eventCollisionDto.getDateSuggestions() != null);
        assert (eventCollisionDto.getDateSuggestions().size() <= 10);
    }
}
