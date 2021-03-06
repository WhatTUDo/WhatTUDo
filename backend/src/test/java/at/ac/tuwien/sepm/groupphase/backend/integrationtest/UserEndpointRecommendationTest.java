package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


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

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    PlatformTransactionManager txm;

    TransactionStatus txstatus;

    @BeforeEach
    public void setupDBTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txstatus = txm.getTransaction(def);
        assumeTrue(txstatus.isNewTransaction());
        txstatus.setRollbackOnly();
    }

    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void getRecommendedEvents_shouldReturn_correctEvent() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        ApplicationUser user = userService.getUserByName("Dillon Dingle");
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(orga)));
        Location location = new Location("Test Location", "Test Adress", "Zip", 0, 0);
        List<Label> labels1 = new ArrayList<>();
        List<Label> labels2 = new ArrayList<>();
        List<Label> labels3 = new ArrayList<>();
        List<Event> events1 = new ArrayList<>();
        List<Event> events2 = new ArrayList<>();
        Label label1 = new Label("TestLabel1");
        Label label2 = new Label("TestLabel2");
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar, location);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar, location);
        Event event3 = new Event("Test Event 3", LocalDateTime.now().plusDays(20), LocalDateTime.now().plusDays(21), calendar, location);
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
        event1.setLocation(location);
        event2.setLocation(location);
        event3.setLocation(location);
        location.setEvents(List.of(event1,event2,event3));
        locationRepository.save(location);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        labelRepository.save(label1);
        labelRepository.save(label2);
        AttendanceStatus attend1 = attendanceRepository.save(new AttendanceStatus(user, event1, AttendanceStatusPossibilities.INTERESTED));
        AttendanceStatus attend2 = attendanceRepository.save(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.ATTENDING));

        List<EventDto> recommendedEvent = new ArrayList<>(userEndpoint.getRecommendedEvents(user.getId()));
        assert (recommendedEvent.size() > 0);

    }

    @WithMockUser(username = "Dillon Dingle", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void ifNoRecommendableEvents_getRecommendedEvents_shouldReturn_anyEvent() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        ApplicationUser user = userService.getUserByName("Dillon Dingle");
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
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar);
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

        Set<EventDto> recommendedEvent = userEndpoint.getRecommendedEvents(user.getId());
        assertNotNull(recommendedEvent);
        assert (recommendedEvent.size() > 0);

    }
}
