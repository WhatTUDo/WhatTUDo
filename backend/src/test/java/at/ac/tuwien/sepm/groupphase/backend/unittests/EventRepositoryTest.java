package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    OrganisationRepository organisationRepository;
    //TODO: test stuff

    @Test
    public void repoBasics() {
        //noinspection ConstantConditions
//        assertThrows(InvalidDataAccessApiUsageException.class, () -> eventRepository.save(null));
//        assertThrows(NullPointerException.class, () -> eventRepository.save(new Event(null,null,null,null,null,null)));
//        eventRepository.save(new Event("Test Name", LocalDateTime.now(),LocalDateTime.now().plusHours(2),new Location("FSINF","Gußhausstraße 1","1220","48.1997","16.3675"),new Label[1], new Comment[1]));
//        eventRepository.save(new Event("Test Name", LocalDateTime.now(),LocalDateTime.now().plusHours(2),new Location("FSINF","Gußhausstraße 1","1220","48.1997","16.3675"),new Label[1], new Comment[1]));
    }


}
