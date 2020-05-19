package at.ac.tuwien.sepm.groupphase.backend.servicetests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {

    @Autowired
    EventService service;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    OrganizationRepository organizationRepository;


    @Test
    public void save_shouldReturn_sameEvent() {
        Organization orga = organizationRepository.save(new Organization("Test Organization Service 1"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 1", Collections.singletonList(orga)));
        Event eventEntity = new Event("Test Name", LocalDateTime.of(2020, 1, 1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        Event gottenEvent = service.save(new Event("Test Name", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar));

        assertEquals(eventEntity.getName(),gottenEvent.getName());
        assertEquals(eventEntity.getEndDateTime(),gottenEvent.getEndDateTime());
        assertEquals(eventEntity.getStartDateTime(),gottenEvent.getStartDateTime());
        assertEquals(eventEntity.getCalendar(),gottenEvent.getCalendar());
    }

    @Test
    public void save_thenRead_shouldReturn_sameEvent() {
        Organization orga = organizationRepository.save(new Organization("Test Organization Service 2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 2", Collections.singletonList(orga)));
        Event eventEntity = new Event("Test Name", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        Event returnedEvent = service.save(eventEntity);
        Event gottenEvent = service.findById(returnedEvent.getId());
        Calendar returnedCal = returnedEvent.getCalendar();
        Calendar gottenCal = gottenEvent.getCalendar();
        assertEquals(returnedEvent.getId(),gottenEvent.getId());
        assertEquals(returnedEvent.getName(),gottenEvent.getName());
        assertEquals(returnedEvent.getEndDateTime(),gottenEvent.getEndDateTime());
        assertEquals(returnedEvent.getStartDateTime(),gottenEvent.getStartDateTime());
        assertEquals(returnedCal.getName(),gottenCal.getName());
        assertEquals(returnedCal.getId(),gottenCal.getId());
    }

    @Test
    public void save_withoutCorrectParam_shouldReturn_ValidationException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization Service 3"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(orga)));
        Event event1 = new Event("", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        Event event2 = new Event("Test Event", LocalDateTime.of(2020,1,2,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        assertThrows(ValidationException.class, () -> service.save(event1));
        assertThrows(ValidationException.class, () -> service.save(event2));
        assertThrows(ValidationException.class, () -> service.save(null));
    }

    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsValidationException(){
        Organization orga = organizationRepository.save(new Organization("Test Organization Service 4"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 4", Collections.singletonList(orga)));

        Event notSavedEvent = new Event("Non Existent", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        assertThrows(ValidationException.class, () -> service.delete(notSavedEvent));
    }

    @Test
    public void delete_savedEvent_findBYIdReturnsNotFound(){
        Organization orga = organizationRepository.save(new Organization("Test Organization Service 5"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 5", Collections.singletonList(orga)));

        Event eventEntity = new Event("Delete Event Test", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        Event event = service.save(eventEntity);
        service.delete(event);
        assertThrows(NotFoundException.class, () -> service.findById(event.getId()));
    }


    @Test
    public void deleteEvent_withIdDoesNotExist_throwsNotFoundException(){
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 6", Collections.singletonList(new Organization())));

        Event eventEntity = new Event(0,"Delete Event Test", LocalDateTime.of(2020,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        assertThrows(NotFoundException.class, () -> service.delete(eventEntity));
    }




}
