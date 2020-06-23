package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ICalService;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class ICalServiceTest {
    @Autowired
    ICalService iCalService;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    LocationRepository locationRepository;

    private Calendar calendar1;
    private Calendar calendar2;
    private Location room;

    @BeforeEach
    @Transactional
    public void createTestData() {
        Organization orga = new Organization("Test Orga");
        room = locationRepository.save(new Location("Test Room", "Test Rd.", "1040", 123.45, 123.45));
        calendar1 = calendarRepository.save(new Calendar("Test Calendar 1", Collections.singletonList(orga), "Description"));
        calendar2 = calendarRepository.save(new Calendar("Test Calendar 2", Collections.singletonList(orga), "Description"));
        for (int i = 0; i < 6; i++) {
            eventRepository.save(new Event(
                "Event " + i,
                LocalDateTime.of(2020, 1, i + 1, 10, 0, 0),
                LocalDateTime.of(2020, 1, i + 1, 12, 0, 0),
                i < 3 ? calendar1 : calendar2,
                room,
                "Description " + i
            ));
        }
    }

    @Test
    @Transactional
    public void interop_working() {
        ICalendar calendar = iCalService.getCalendar(calendar1.getId());
        List<VEvent> events = calendar.getEvents();
        assertAll(
            () -> assertEquals(3, events.size()),
            () -> assertEquals(calendar1.getEvents().get(0).getName(), events.get(0).getSummary().getValue()),
            () -> assertEquals(calendar1.getEvents().get(0).getStartDateTime().atZone(ZoneId.systemDefault()).toInstant(), events.get(0).getDateStart().getValue().toInstant()),
            () -> assertEquals(calendar1.getEvents().get(0).getEndDateTime().atZone(ZoneId.systemDefault()).toInstant(), events.get(0).getDateEnd().getValue().toInstant()),
            () -> assertEquals(calendar1.getEvents().get(0).getLocation().getName(), events.get(0).getLocation().getValue()),
            () -> assertEquals(calendar1.getEvents().get(0).getDescription(), events.get(0).getDescription().getValue())
        );
    }
}
