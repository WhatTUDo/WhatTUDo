package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.ChangeUserPasswordEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.hibernate.event.service.spi.EventListenerRegistrationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserEndpointTest {
    @Autowired
    UserEndpoint userEndpoint;

    @Autowired
    UserService userService;

    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    ChangeUserPasswordEndpoint userPasswordEndpoint;

    @Autowired
    PasswordEncoder passwordEncoder;


    @WithMockUser
    @Test
    public void saveNewUser_shouldReturn_UserDto_withEncodedPassword() {
        IncomingUserDto userDto = new IncomingUserDto(0, "Test", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());
        assertNotEquals(userDto.getId(), savedUserDto.getId());
//        assertNotEquals(userDto.getPassword(), savedUserDto.getPassword());

    }

    @Test
    public void updateUser() {
        IncomingUserDto userDto = new IncomingUserDto(null, "user1", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());

        LoggedInUserDto userDto1 = new LoggedInUserDto(savedUserDto.getId(), "user2", null);

        LoggedInUserDto updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(userDto1.getName(), updateUser.getName());


        userDto1 = new LoggedInUserDto(savedUserDto.getId(), null, "user43@test.com");

        updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(savedUserDto.getId(), updateUser.getId());
        assertEquals(userDto1.getEmail(), updateUser.getEmail());


    }

    @Test
    public void changePassword() {
        IncomingUserDto userDto = new IncomingUserDto(null, "changePasswordUser", "changepass@test.com", "hunter3");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        LoggedInUserDto changePasswordUserDto = userPasswordEndpoint.changeUserPassword(new ChangePasswordDto(savedUserDto.getName(), savedUserDto.getEmail(), "hunter3", "hunter4"));

//        assertTrue(passwordEncoder.matches("hunter4", changePasswordUserDto()));


    }

    @Test
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

        EventDto recommendedEvent = userEndpoint.getRecommendedEvent(user.getId());
        assert (recommendedEvent != null);
        assertEquals(recommendedEvent.getId(), event3.getId());
    }

    @Test
    @Transactional
    public void ifNoRecommendableEvents_getRecommendedEvents_shouldReturn_anyEvent() {
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
        Event event3 = new Event("Test Event 3", LocalDateTime.of(2021, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
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

        EventDto recommendedEvent = userEndpoint.getRecommendedEvent(user.getId());
        assert (recommendedEvent != null);
    }


}
