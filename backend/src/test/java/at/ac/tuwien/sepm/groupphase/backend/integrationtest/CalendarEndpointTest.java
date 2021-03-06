package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.CalendarEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class CalendarEndpointTest {

    @Autowired
    CalendarEndpoint calendarEndpoint;

    @Autowired
    EventEndpoint eventEndpoint;

    @Autowired
    LocationRepository locationRepository;


    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void createCalendar_returnsCalendar() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);

        CalendarCreateDto calendarDto = new CalendarCreateDto("Save", 1);

        CalendarDto calendarSaved = calendarEndpoint.create(calendarDto);

        assertEquals(calendarDto.getName(), calendarSaved.getName());
    }

    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void createCalendar_withDescription_returnsCalendar() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);

        CalendarCreateDto calendarDto = new CalendarCreateDto("Save", 1, "Description");

        CalendarDto calendarSaved = calendarEndpoint.create(calendarDto);

        assertEquals(calendarDto.getName(), calendarSaved.getName());
        assertEquals(calendarDto.getDescription(), calendarSaved.getDescription());
    }

    @Transactional
    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void searchCalendar_byCalendarName_byEventName_returnsSavedCalendar() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Location loc = new Location("Test Location", "Test Adress", "Zip", 0, 0);
        Location location = locationRepository.save(loc);
        CalendarDto calendarDto = calendarEndpoint.create(new CalendarCreateDto("Search", 1));

        List<CalendarDto> found = calendarEndpoint.searchCalendarCombo("Search");

        assertEquals(1, found.size());


        eventEndpoint.post(new EventDto(1, "SearchEvent", LocalDateTime.of(2021, 2, 2, 14, 0), LocalDateTime.of(2021, 2, 2, 15, 0), calendarDto.getId(), location.getId()));
        found = calendarEndpoint.searchCalendarCombo("SearchEvent");
        assertEquals(1, found.size());

    }


    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void deleteCalendar_findCalendarWillReturnNotFound() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);

        CalendarDto calendarDto = calendarEndpoint.create(new CalendarCreateDto("Delete", 1));

        calendarEndpoint.deleteCalendar(calendarDto.getId());

        assertThrows(ResponseStatusException.class, () -> calendarEndpoint.getById(calendarDto.getId()));
    }

    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void editCalendar_returnsCalendarWithUpdatedValues() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        List<Integer> e = new ArrayList<>();

        CalendarCreateDto calendarDto = new CalendarCreateDto("Save to update", 1);

        CalendarDto calendarSaved = calendarEndpoint.create(calendarDto);

        CalendarDto update = new CalendarDto(calendarSaved.getId(), "Updated", Collections.singletonList(orga.getId()), e);

        assertEquals(update.getName(), calendarEndpoint.editCalendar(update).getName());
    }

    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1", "MOD_2", "MEMBER_2"})
    @Test
    public void updateOrganizationsForCalendar() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        List<Integer> org = new ArrayList<>();
        org.add(1);
        CalendarCreateDto calendarDto = new CalendarCreateDto("Save to update 2", 1);

        CalendarDto calendarSaved = calendarEndpoint.create(calendarDto);
        Organization orga1 = new Organization("Test Org 2");
        orga1.setId(2);
        org.add(2);

        CalendarDto update = new CalendarDto(calendarSaved.getId(), calendarSaved.getName(), org, calendarSaved.getEventIds());

        assertEquals(update.getOrganizationIds(), calendarEndpoint.updateOrganizationsForCalendar(update).getOrganizationIds());


    }
}
