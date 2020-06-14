package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserEndpointRecommendationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserEndpoint userEndpoint;

    @Autowired
    CalendarRepository calendarRepository;

    @Mock
    OrganizationRepository organizationRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    LabelRepository labelRepository;



   /* @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void getRecommendedEvents_shouldReturn_correctEvent() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);
        ApplicationUser user = userService.getUserByName("Person 1");
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(orga)));
        List<Label> labels1 = new ArrayList<>();
        List<Label> labels2 = new ArrayList<>();
        List<Label> labels3 = new ArrayList<>();
        List<Event> events1 = new ArrayList<>();
        List<Event> events2 = new ArrayList<>();
        Label label1 = new Label("TestLabel1");
        Label label2 = new Label("TestLabel2");
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event3 = new Event("Test Event 3", LocalDateTime.now().plusDays(20), LocalDateTime.now().plusDays(21), calendar);
        events1.add(event1);
        events1.add(event3);
        events2.add(event1);
        events2.add(event2);
        labels1.add(label1);
        labels2.add(label1);
        labels2.add(label2);
        labels3.add(label1);
        event1.setLabels(labels1);
        event2.setLabels(labels2);
        event3.setLabels(labels3);
        label1.setEvents(events1);
        label2.setEvents(events2);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        labelRepository.save(label1);
        labelRepository.save(label2);
        AttendanceStatus attend1 = attendanceRepository.save(new AttendanceStatus(user, event1, AttendanceStatusPossibilities.INTERESTED));
        AttendanceStatus attend2 = attendanceRepository.save(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.ATTENDING));

        List<EventDto> recommendedEvent = userEndpoint.getRecommendedEvents(user.getId());
        assert (recommendedEvent != null);
        assert (recommendedEvent.size() > 0);
        assertEquals (recommendedEvent.get(0).getId(), event3.getId());

    }*/

    /*@WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void ifNoRecommendableEvents_getRecommendedEvents_shouldReturn_anyEvent() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);
        ApplicationUser user = userService.getUserByName("Person 1");
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(orga)));
        List<Label> labels1 = new ArrayList<>();
        List<Label> labels2 = new ArrayList<>();
        List<Label> labels3 = new ArrayList<>();
        List<Event> events1 = new ArrayList<>();
        List<Event> events2 = new ArrayList<>();
        List<Event> events3 = new ArrayList<>();
        Label label1 = new Label("TestLabel1");
        Label label2 = new Label("TestLabel2");
        Label label3 = new Label("TestLabel3");
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event3 = new Event("Test Event 3", LocalDateTime.now().plusDays(20), LocalDateTime.now().plusDays(21), calendar);
        events1.add(event1);
        events2.add(event1);
        events2.add(event2);
        events3.add(event3);
        labels1.add(label1);
        labels2.add(label1);
        labels2.add(label2);
        labels3.add(label3);
        event1.setLabels(labels1);
        event2.setLabels(labels2);
        event3.setLabels(labels3);
        label1.setEvents(events1);
        label2.setEvents(events2);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        labelRepository.save(label1);
        labelRepository.save(label2);
        labelRepository.save(label3);
        AttendanceStatus attend1 = attendanceRepository.save(new AttendanceStatus(user, event1, AttendanceStatusPossibilities.INTERESTED));
        AttendanceStatus attend2 = attendanceRepository.save(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.ATTENDING));

        List<EventDto> recommendedEvent = userEndpoint.getRecommendedEvents(user.getId());
        assert (recommendedEvent != null);
        assert (recommendedEvent.size() > 0);

    }*/
}
