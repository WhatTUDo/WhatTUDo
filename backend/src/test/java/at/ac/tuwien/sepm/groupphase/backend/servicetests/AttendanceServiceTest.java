package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AttendanceServiceTest {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarRepository calendarRepository;


    @Test
    public void create_shouldReturn_AttendanceStatus() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Organization organization = organizationRepository.save(new Organization("BesterTestnameEver"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus returnedAttendance = attendanceService.create(attendance);
        assertEquals(attendance.getUser(), returnedAttendance.getUser());
        assertEquals(attendance.getEvent(), returnedAttendance.getEvent());

    }

    /*
    @Test
    public void save_thenChange_shouldReturn_newAttendanceStatus() {

    }

    @Test
    public void save_withUserNotExisting_shouldThrow_NotFoundException() {

    }

    @Test
    public void save_withUserNotExisting_shouldThrow_NotFoundException() {

    }

    @Test
    public void save_withUserNotExisting_shouldThrow_NotFoundException() {

    }
*/

}
