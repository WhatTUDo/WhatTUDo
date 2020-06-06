package at.ac.tuwien.sepm.groupphase.backend.servicetests;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventCollisionService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventCollisionTest {

    @Autowired
    EventCollisionService eventCollisionService;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    LabelService labelService;

    Organization organization;

    @Before
    public void generateData() {
        this.organization = organizationRepository.save(new Organization("BesterTestnameEver"));

    }

    @Test
    public void createEvent_CheckForCollisions_shouldReturnEmptyList() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Dorian", "grazie@gmx.com", "pwdsuperstrong"));
        Calendar calendar = calendarRepository.save(new Calendar("Katzenkalenderreleases", Collections.singletonList(organization)));
        Event event = new Event("Adventskatzenkalender", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar);
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        List<Label> labels = new ArrayList<>();
        List<AttendanceStatus> attendanceStatuses = new ArrayList<>();
        attendanceStatuses.add(attendance);
        labels.add(new Label(1, "Bla", null));
        event.setLabels(labels);

        List<EventCollision> eventCollisions = eventCollisionService.getEventCollisions(event, 3, 12L);
        assertEquals(0, eventCollisions.size());
    }

    @Test
    public void saveEvents_createEventWithCollidingDates_CheckForCollisions_ShouldReturnCollisionList() {
        Calendar calendar = calendarRepository.save(new Calendar("Katzenkalenderreleases", Collections.singletonList(organization)));
        ApplicationUser user = userRepository.save(new ApplicationUser("Dorian", "grazie@gmx.com", "pwdsuperstrong"));

        Event event1 = new Event("Adventskatzenkalender", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar);
        Event eventToTest = new Event("Adventskatzenkalender2", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar);
        List<Label> labels = new ArrayList<>();
        AttendanceStatus attendance = new AttendanceStatus(user, event1, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus attendance2 = new AttendanceStatus(user, eventToTest, AttendanceStatusPossibilities.ATTENDING);
        List<AttendanceStatus> attendanceStatuses = new ArrayList<>();
        attendanceStatuses.add(attendance);
        event1.setAttendanceStatuses(attendanceStatuses);
        labels.add(new Label(1, "Bla", null));
        labels.add(new Label(2, "Bli", null));
        labels.add(new Label(2, "Blu", null));
        event1.setLabels(labels);
        eventRepository.save(event1);
        List<EventCollision> eventCollisions = eventCollisionService.getEventCollisions(eventToTest, 3, 12L);

        assertEquals(1, eventCollisions.size());


    }

    @Test
    public void saveEvents_createEventWithCollidingLabels_CheckForCollisions_ShouldReturnCollisionList() {
        Calendar calendar = calendarRepository.save(new Calendar("Katzenkalenderreleases", Collections.singletonList(organization)));
        ApplicationUser user = userRepository.save(new ApplicationUser("Dorian", "grazie@gmx.com", "pwdsuperstrong"));

    }
}
