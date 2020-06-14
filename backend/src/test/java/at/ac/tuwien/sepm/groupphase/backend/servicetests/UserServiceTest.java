package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    EventRepository eventRepository;

    @Autowired
    CustomUserDetailService userService;
    @Autowired
    AttendanceService attendanceService;

    @Test
    public void when_savedUser_findAllUsers_shouldReturnListContainingUser() {
        userService.saveNewUser(new ApplicationUser("TestUser", "testy@test.com", "hunter2"));
        List<ApplicationUser> users = userRepository.findAll();
        assert (users.size() > 0);
    }

    @Test
    public void when_savedUser_findAllUsers_shouldReturnCorrectUserDetails() {
        userService.saveNewUser(new ApplicationUser("TestUser 1", "testy1@test.com", "hunter2"));
        ApplicationUser user = (ApplicationUser) userService.loadUserByUsername("TestUser");
        assert (user.getId() != null && user.getId() != 0);

    }

    @Test
    public void updateUser() {

        ApplicationUser user = new ApplicationUser("user1", "testy@test.com", "hunter2");

        ApplicationUser savedUser = userService.saveNewUser(user);


        ApplicationUser user1 = new ApplicationUser("user2", "", "");
        user1.setId(savedUser.getId());

        ApplicationUser updateUser = userService.updateUser(user1);

        assertEquals(user1.getName(), updateUser.getName());


        user1 = new ApplicationUser("", "", "user43@test.com");
        user1.setId(savedUser.getId());
        updateUser = userService.updateUser(user1);

        assertEquals(savedUser.getId(), updateUser.getId());
        assertEquals(user1.getEmail(), updateUser.getEmail());


    }

    /*@Test
    @Transactional
    public void getRecommendedEvents_shouldReturn_correctEvent() {
        ApplicationUser user = userService.saveNewUser(new ApplicationUser("TestUser 1", "testy1@test.com", "hunter2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(new Organization())));
        List<Label> labels1 = new ArrayList<>();
        List<Label> labels2 = new ArrayList<>();
        List<Label> labels3 = new ArrayList<>();
        List<Event> events1 = new ArrayList<>();
        List<Event> events2 = new ArrayList<>();
        Label label1 = new Label("TestLabel1");
        Label label2 = new Label("TestLabel2");
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        Event event3 = new Event("Test Event 3", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);

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

        List<Event> recommendedEvent = userService.getRecommendedEvents(user.getId());
        assert (recommendedEvent != null);
        assert (recommendedEvent.size() > 0);
        assert (recommendedEvent.contains(event3));

    }*/


    /*@Test
    @Transactional
    public void ifNoRecommendableEvents_getRecommendedEvents_shouldReturn_randomEvents() {
        ApplicationUser user = userService.saveNewUser(new ApplicationUser("TestUser 1", "testy1@test.com", "hunter2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(new Organization())));
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

        List<Event> recommendedEvent = userService.getRecommendedEvents(user.getId());
        assert (recommendedEvent != null);
        assert (recommendedEvent.size() > 0);
    }*/


}
