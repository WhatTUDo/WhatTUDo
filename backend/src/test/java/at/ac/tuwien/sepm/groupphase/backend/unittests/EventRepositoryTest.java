package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

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

    @Test
    public void repoBasics() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar", Collections.singletonList(orga)));

        assertThrows(InvalidDataAccessApiUsageException.class, () -> eventRepository.save(null));
        assertThrows(NullPointerException.class, () -> eventRepository.save(new Event(null,null,null,null)));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020,1,1,17,30),LocalDateTime.of(2020,1,1,18,0),calendar));
    }

    @Test
    public void eventBasics() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar", Collections.singletonList(orga)));
        Event event = eventRepository.save(new Event("Test Name", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar));

        assertEquals("Test Name", event.getName());
        assertEquals(LocalDateTime.of(2020,1,1,15,30), event.getStartDateTime());
        assertEquals(LocalDateTime.of(2020,1,1,16,0), event.getEndDateTime());
        assertEquals(calendar, event.getCalendar());

        eventRepository.delete(event);

        assertEquals( Optional.empty(),eventRepository.findById(event.getId()));
    }




}