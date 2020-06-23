package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.util.ICalMapper;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class ICalMapperUnitTest {
    private Event event;
    private VEvent vEvent;

    @BeforeEach
    @Transactional
    public void createTestData() {
        ICalMapper iCalMapper = new ICalMapper();
        event = new Event(
            "Event",
            LocalDateTime.of(2020, 1, 1, 10, 0, 0),
            LocalDateTime.of(2020, 1, 1, 12, 0, 0),
            null,
            new Location("Test Room", "Test Rd.", "1040", 123.45, 123.45),
            "Description"
        );
        event.setId(1);
        vEvent = iCalMapper.mapEvent(event);
    }

    @Test
    @Transactional
    public void time_working() {
        assertAll(
            () -> assertEquals(event.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant(), vEvent.getDateStart().getValue().toInstant()),
            () -> assertEquals(event.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant(), vEvent.getDateEnd().getValue().toInstant())
        );
    }

    @Test
    @Transactional
    public void name_working() {
        assertEquals(event.getName(), vEvent.getSummary().getValue());
    }

    @Test
    @Transactional
    public void location_working() {
        assertEquals(event.getLocation().getName(), vEvent.getLocation().getValue());
    }

    @Test
    @Transactional
    public void description_working() {
        assertEquals(event.getDescription(), vEvent.getDescription().getValue());
    }
}
