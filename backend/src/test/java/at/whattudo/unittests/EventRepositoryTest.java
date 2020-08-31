package at.whattudo.unittests;

import at.whattudo.entity.Calendar;
import at.whattudo.entity.Event;
import at.whattudo.entity.Organization;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext
public class EventRepositoryTest {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    public void repoBasics() {
        Organization orga = organizationRepository.save(new Organization("Test Organization"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar", Collections.singletonList(orga)));

        assertThrows(InvalidDataAccessApiUsageException.class, () -> eventRepository.save(null));
        //assertThrows(NullPointerException.class, () -> eventRepository.save(new Event(null,null,null,null,null,null)));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020, 1, 1, 17, 30), LocalDateTime.of(2020, 1, 1, 18, 0), calendar));
    }

    @Test
    public void eventBasics() {
        Organization orga = organizationRepository.save(new Organization("Test Organization"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar", Collections.singletonList(orga)));
        Event event = eventRepository.save(new Event("Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar));

        assertEquals("Test Name", event.getName());
        assertEquals(LocalDateTime.of(2020, 1, 1, 15, 30), event.getStartDateTime());
        assertEquals(LocalDateTime.of(2020, 1, 1, 16, 0), event.getEndDateTime());
        assertEquals(calendar, event.getCalendar());

        eventRepository.delete(event);

        assertEquals(Optional.empty(), eventRepository.findById(event.getId()));
    }

    @Test
    public void whenQueryForEventsBetweenDates_shouldEventsExist_returnEvents() {
        Organization orga = organizationRepository.save(new Organization("Test Organization"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar", Collections.singletonList(orga)));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar));
        eventRepository.save(new Event("Test Name", LocalDateTime.of(2020, 1, 3, 15, 30), LocalDateTime.of(2020, 1, 3, 16, 0), calendar));

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 8, 23, 59);

        List<Event> events = eventRepository.findAllByStartDateTimeBetween(start, end);

        assertNotEquals(0, events.size());
        Event retrievedEvent1 = events.get(0);
        Event retrievedEvent2 = events.get(1);

        assert (start.isBefore(retrievedEvent1.getStartDateTime()));
        assert (start.isBefore(retrievedEvent2.getStartDateTime()));

        assert (end.isAfter(retrievedEvent1.getStartDateTime()));
        assert (end.isAfter(retrievedEvent2.getStartDateTime()));

    }


}
