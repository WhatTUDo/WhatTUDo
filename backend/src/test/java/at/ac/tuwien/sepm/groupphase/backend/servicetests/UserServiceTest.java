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


}
