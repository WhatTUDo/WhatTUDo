package at.whattudo.unittests;


import at.whattudo.entity.Event;
import at.whattudo.entity.Location;
import at.whattudo.util.ICalMapper;
import biweekly.component.VEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
            () -> assertEquals(event.getStartDateTime().atZone(ZoneId.of("UTC")).toInstant(), vEvent.getDateStart().getValue().toInstant()),
            () -> assertEquals(event.getEndDateTime().atZone(ZoneId.of("UTC")).toInstant(), vEvent.getDateEnd().getValue().toInstant())
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
