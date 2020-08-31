package at.whattudo.servicetests;


import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.exception.NotFoundException;
import at.whattudo.service.CalendarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class CalendarServiceTest {
    @Autowired
    CalendarService calendarService;



    public Organization createOrga() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        return orga;
    }

    @Test
    public void saveNewCalendar_returnsCalendar() {

        Calendar calendar = new Calendar("save", Collections.singletonList(createOrga()));
        Calendar saved = calendarService.save(calendar);
        assertEquals("save", saved.getName());
        assertEquals(calendar.getOrganizations(), saved.getOrganizations());
    }

    @Test
    public void saveNewCalendar_withDescription_returnsCalendar() {

        Calendar calendar = new Calendar("save", Collections.singletonList(createOrga()), "Description");
        Calendar saved = calendarService.save(calendar);
        assertEquals("save", saved.getName());
        assertEquals(calendar.getOrganizations(), saved.getOrganizations());
        assertEquals("Description", saved.getDescription());
    }

    @Test
    public void findByIdAndName_returnsCreatedCalendar() {
        Calendar calendar = calendarService.save(new Calendar("find", Collections.singletonList(createOrga())));
        assertEquals(calendar.getName(), calendarService.findById(calendar.getId()).getName());
        assertEquals(calendar.getOrganizations().size(), calendarService.findByName(calendar.getName()).get(0).getOrganizations().size());
        assertEquals(calendar.getId(), calendarService.findByName(calendar.getName()).get(0).getId());
    }

    @Test
    public void deleteCalendar_findByCalendarId_returnsNotFoundException() {
        Calendar calendar = calendarService.save(new Calendar("delete", Collections.singletonList(createOrga())));
        assertEquals(calendar.getName(), calendarService.findById(calendar.getId()).getName());

        calendarService.delete(calendar.getId());

        assertThrows(NotFoundException.class, () -> calendarService.findById(calendar.getId()));

    }

    @Test
    public void updateCalendarName_returnsUpdatedCalendarWithUpdatedName() {
        Calendar calendar = calendarService.save(new Calendar("update", Collections.singletonList(createOrga())));
        Calendar calendar1 = new Calendar("updated", calendar.getOrganizations());
        calendar1.setId(calendar.getId());

        assertEquals(calendar1.getName(), calendarService.update(calendar1).getName());

    }


}
