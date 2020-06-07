package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
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
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    CustomUserDetailService userService;
    @Autowired
    AttendanceService attendanceService;

    @Test
    public void when_savedUser_findAllUsers_shouldReturnListContainingUser() {
        userService.saveNewUser(new ApplicationUser("TestUser", "testy@test.com", "hunter2"));
        List users = userRepository.findAll();
        assert (users.size() > 0);
    }

    @Test
    public void when_savedUser_findAllUsers_shouldReturnCorrectUserDetails() {
        userService.saveNewUser(new ApplicationUser("TestUser 1", "testy1@test.com", "hunter2"));
        ApplicationUser user = (ApplicationUser) userService.loadUserByUsername("TestUser");
        assert (user.getId() != null && user.getId() != 0);

    }

    @Test
    public void getRecommendedEvents_shouldReturn_correctEvent() {
        ApplicationUser user = userService.saveNewUser(new ApplicationUser("TestUser 1", "testy1@test.com", "hunter2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar Service 3", Collections.singletonList(new Organization())));
        List<Label> labels1 = new ArrayList<>();
        List<Label> labels2 = new ArrayList<>();
        List<Label> labels3 = new ArrayList<>();
        Label label1 = new Label("TestLabel1");
        Label label2 = new Label("TestLabel2");
        labels1.add(label1);
        labels2.add(label1);
        labels2.add(label2);
        labels3.add(label1);
        Event event1 = new Event("Test Event 1", LocalDateTime.of(2021,1,1,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        event1.setLabels(labels1);
        Event event2 = new Event("Test  Event 2", LocalDateTime.of(2021,1,2,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        event2.setLabels(labels2);
        AttendanceStatus attend1 = attendanceRepository.save(new AttendanceStatus(user,event1,AttendanceStatusPossibilities.INTERESTED));
        AttendanceStatus attend2 = attendanceRepository.save(new AttendanceStatus(user,event2,AttendanceStatusPossibilities.INTERESTED));
        Event event3 = new Event("Test Event 3", LocalDateTime.of(2021,1,2,15,30),LocalDateTime.of(2020,1,1,16,0),calendar);
        event3.setLabels(labels3);
        Event recommendedEvent = userService.getRecommendedEvent(user.getId());
        assert(recommendedEvent!=null);
        assertEquals(recommendedEvent.getId(),event3.getId());

    }

}
