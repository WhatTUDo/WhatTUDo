package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        EventDto eventDto = new EventDto(1, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
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
        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        EventDto gottenEvent = endpoint.getById(returnedEvent.getId());
        Optional<Calendar> fetchCalendar = calendarRepository.findById(returnedEvent.getCalendarId());
        Calendar returnedCalendar = fetchCalendar.get();
        fetchCalendar = calendarRepository.findById(gottenEvent.getCalendarId());
        Calendar gottenCalendar = fetchCalendar.get();

        assertEquals(gottenEvent.getName(), returnedEvent.getName());
        assertEquals(gottenEvent.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(gottenEvent.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(gottenEvent.getId(), returnedEvent.getId());
        assertEquals(returnedCalendar.getName(), gottenCalendar.getName());
        assertEquals(returnedCalendar.getId(), gottenCalendar.getId());

    }

    @Test
    public void save_withoutName_shouldReturn_ResponseStatusException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation3"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar3", Collections.singletonList(orga)));
        EventDto eventDto1 = new EventDto(null, "", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto eventDto2 = new EventDto(null, "Test Event", LocalDateTime.of(2020, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto2));
    }

    @Test
    public void save_withNoArgs_shouldReturn_ResponseStatusException() {
        EventDto eventDto = new EventDto();
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto));
    }

    @Test
    public void save_nullObject_shouldReturn_ResponseStatusException() {
        assertThrows(ResponseStatusException.class, () -> endpoint.post(null));
    }

    @Test
    public void get_validID_shouldReturn_EventWithSpecifiedID() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation9"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar9", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(1, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertNotNull(returnedEvent);
        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

    @Test
    public void get_invalidID_shouldReturn_ResponseStatusException_With404Code() {
        int uselessID = 123456;
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(uselessID));
        try {
            endpoint.getById(uselessID);
        } catch (ResponseStatusException e) {
            assertEquals(404, e.getStatus().value());
        }
    }

    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsResponseStatusException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation4"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar4", Collections.singletonList(orga)));
        EventDto notSavedEvent = new EventDto(null, "Non Existent", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.deleteEvent(notSavedEvent));
    }


    @Test
    public void delete_savedEvent_findBYIdReturnsResponseException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation5"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar5", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(null, "Delete Event", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        endpoint.deleteEvent(returnedEvent);
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(returnedEvent.getId()));
    }


    @Test
    public void updateEntityValues_shouldReturn_correctChanges() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation6"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar6", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(2, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        EventDto finalEvent = endpoint.editEvent(eventDtoChanges);

        assertEquals(finalEvent.getName(), eventDtoChanges.getName());
        assertEquals(finalEvent.getEndDateTime(), eventDtoChanges.getEndDateTime());
        assertEquals(finalEvent.getStartDateTime(), eventDtoChanges.getStartDateTime());
        assertEquals(finalEvent.getCalendarId(), eventDtoChanges.getCalendarId());
    }

    @Test
    public void updateEntityStartDateBefore2020_throwsResponseException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation7"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar7", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(3, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2000, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

    @Test
    public void updateEntityStartDateBeforeEndDate_throwsResponseException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation8"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar8", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(4, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2022, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

}

