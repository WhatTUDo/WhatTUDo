package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

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
        EventDto eventDto = new EventDto("Test Name", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar);
        EventDto returnedEvent = endpoint.post(eventDto);
        Calendar DtoCalendar = eventDto.getCalendar();
        Calendar returnedDtoCalendar = returnedEvent.getCalendar();

        assertEquals(eventDto.getName(),returnedEvent.getName());
        assertEquals(eventDto.getEndDate(),returnedEvent.getEndDate());
        assertEquals(eventDto.getStartDate(),returnedEvent.getStartDate());
        assertEquals(eventDto.getCalendar(),returnedEvent.getCalendar());
    }

    @Test
    public void save_thenRead_shouldReturn_sameEvent() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar2", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto("Test Name", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar);
        EventDto returnedEvent = endpoint.post(eventDto);
        EventDto gottenEvent = endpoint.getById(returnedEvent.getId());
        Calendar returnedCalendar = returnedEvent.getCalendar();
        Calendar gottenCalendar = gottenEvent.getCalendar();

        assertEquals(gottenEvent.getName(),returnedEvent.getName());
        assertEquals(gottenEvent.getEndDate(),returnedEvent.getEndDate());
        assertEquals(gottenEvent.getStartDate(),returnedEvent.getStartDate());
        assertEquals(gottenEvent.getId(),returnedEvent.getId());
        assertEquals(returnedCalendar.getName(),gottenCalendar.getName());
        assertEquals(returnedCalendar.getId(),gottenCalendar.getId());

    }

    @Test
    public void save_withoutName_shouldReturn_ResponseStatusException() {
        Organisation orga = organisationRepository.save(new Organisation("Test Organisation3"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar3", Collections.singletonList(orga)));
        EventDto eventDto1 = new EventDto("", LocalDateTime.of(2020,01,01,15,30),LocalDateTime.of(2020,01,01,16,00),calendar);
        EventDto eventDto2 = new EventDto("Test Event", LocalDateTime.of(2020,01,02,15,30),LocalDateTime.of(2020,01,01,16,00),calendar);
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto2));
    }



}
