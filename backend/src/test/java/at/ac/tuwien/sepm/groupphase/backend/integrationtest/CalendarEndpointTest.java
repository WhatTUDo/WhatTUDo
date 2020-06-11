package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.CalendarEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class CalendarEndpointTest {

    @Autowired
    CalendarEndpoint calendarEndpoint;

    @Autowired
    EventEndpoint eventEndpoint;

    @Mock
    OrganizationRepository organizationRepository;

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void createCalendar_returnsCalendar(){
       Organization orga = new Organization("Test Organization");
       orga.setId(1);
       Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);

        CalendarDto calendarDto = new CalendarDto(1, "Save", Collections.singletonList(orga.getId()), null);

        CalendarDto calendarSaved = calendarEndpoint.create(calendarDto);

        assertEquals(calendarDto.getName(), calendarSaved.getName());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void searchCalendar_byCalendarName_byEventName_returnsSavedCalendar(){
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);

        CalendarDto calendarDto = calendarEndpoint.create(new CalendarDto(1, "Search", Collections.singletonList(orga.getId()), null));

        List<CalendarDto> found = calendarEndpoint.searchCalendarCombo("Search");

        assertEquals(1, found.size());

        EventDto eventDto = eventEndpoint.post(new EventDto(1, "SearchEvent", LocalDateTime.of(2021,2,2,14,0), LocalDateTime.of(2021,2,2,15,0), calendarDto.getId()));
        found = calendarEndpoint.searchCalendarCombo("SearchEvent");
        assertEquals(1, found.size());

    }



}
