package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventEndpointTest {

    @Autowired
    EventEndpoint endpoint;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    OrganisationRepository organisationRepository;


    @Test
    public void save_shouldReturn_sameEvent() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation1"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar1", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto("Test Name", LocalDateTime.of(2020, 01, 01, 15, 30), LocalDateTime.of(2020, 01, 01, 16, 00), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

    @Test
    public void save_thenRead_shouldReturn_sameEvent() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar2", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto("Test Name", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        EventDto gottenEvent = endpoint.getById(returnedEvent.getId());
        Optional<Calendar> fetchCalendar = calendarRepository.findById(returnedEvent.getCalendarId());
        Calendar returnedCalendar = fetchCalendar.get();
        fetchCalendar = calendarRepository.findById(gottenEvent.getCalendarId());
        Calendar gottenCalendar = fetchCalendar.get();

        assertEquals(gottenEvent.getName(),returnedEvent.getName());
        assertEquals(gottenEvent.getEndDateTime(),returnedEvent.getEndDateTime());
        assertEquals(gottenEvent.getStartDateTime(),returnedEvent.getStartDateTime());
        assertEquals(gottenEvent.getId(),returnedEvent.getId());
        assertEquals(returnedCalendar.getName(),gottenCalendar.getName());
        assertEquals(returnedCalendar.getId(),gottenCalendar.getId());

    }

    @Test
    public void save_withoutName_shouldReturn_ResponseStatusException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation3"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar3", Collections.singletonList(orga)));
        EventDto eventDto1 = new EventDto("", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar.getId());
        EventDto eventDto2 = new EventDto("Test Event", LocalDateTime.of(2020,01,02,15,30),LocalDateTime.of(2020,01,01,16,00),calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto2));

}

    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsResponseStatusException(){
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation4"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar4", Collections.singletonList(orga)));
        EventDto notSavedEvent = new EventDto("Non Existent", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.deleteEvent(notSavedEvent));
    }


    @Test
    public void delete_savedEvent_findBYIdReturnsResponseException(){
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation5"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar5", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto("Delete Event", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        endpoint.deleteEvent(returnedEvent);
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(returnedEvent.getId()));
    }


    @Test
    public void updateAllEntitiesValues_shouldReturn_correctChanges() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation6"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar6", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto("Test Name", LocalDateTime.of(2020, 01, 01, 15, 30), LocalDateTime.of(2020, 01, 01, 16, 00), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto("Test2", LocalDateTime.of(2021, 01, 01, 15, 30), LocalDateTime.of(2021, 01, 01, 16, 00),calendar.getId());
        
        eventDtoChanges.setId(returnedEvent.getId());

        EventDto finalEvent = endpoint.editEvent(eventDtoChanges);

        assertEquals(finalEvent.getName(), eventDtoChanges.getName());
        assertEquals(finalEvent.getEndDateTime(), eventDtoChanges.getEndDateTime());
        assertEquals(finalEvent.getStartDateTime(), eventDtoChanges.getStartDateTime());
        assertEquals(finalEvent.getCalendarId(), eventDtoChanges.getCalendarId());
    }



}

