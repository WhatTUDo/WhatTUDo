package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.StatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    UserService userService;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    StatusMapper mapper;
    //TODO: fill wit real testdata

    Organization organization;
    @Before
    public void createData(){
         this.organization = organizationRepository.save(new Organization("BesterTestnameEver"));
    }

    @Test
    public void create_shouldReturn_AttendanceStatus() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus returnedAttendance = attendanceService.create(attendance);
        assertEquals(attendance.getUser(), returnedAttendance.getUser());
        assertEquals(attendance.getEvent(), returnedAttendance.getEvent());

    }


    @Test
    public void save_thenChangeStatus_shouldReturn_newAttendanceStatus() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendanceEntity = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus updatedAttendanceEntity = new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED);
        AttendanceStatus savedAttendance = attendanceService.create(attendanceEntity);
        AttendanceStatus updatedAttendance = attendanceService.create(updatedAttendanceEntity);
        assertEquals(updatedAttendance.getUser(),savedAttendance.getUser());
        assertEquals(updatedAttendance.getUser(),attendanceEntity.getUser());
        assertEquals(updatedAttendance.getEvent(),savedAttendance.getEvent());
        assertEquals(updatedAttendance.getEvent(),attendanceEntity.getEvent());
        assertEquals(updatedAttendance.getStatus(),updatedAttendanceEntity.getStatus());
        assertNotEquals(updatedAttendance.getStatus(),savedAttendance.getStatus());
    }

    @Test
    public void when_UserAttend_getUsersByEvent_shouldReturnListContainingUser() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        List users = attendanceService.getUsersByEvent(event);
        assert(users.size()>0);
    }

    @Test
    public void when_savedUser_findAllUsers_shouldReturnCorrectUserDetails() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        ApplicationUser returnedUser = attendanceService.getUsersByEvent(event).get(0);
        assert (returnedUser.getId() != null && returnedUser.getId() != 0);

    }

    @Test
    public void when_UserAttendsEvent_getEventByUser_shouldReturnListContainingEvent() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        List events = attendanceService.getEventByUser(user);
        assert(events.size()>0);
    }

    @Test
    public void when_UserAttendsEvent_getEventByUser_shouldReturnCorrectUserDetails() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        Event returnedEvent = attendanceService.getEventByUser(user).get(0);
        assert (returnedEvent.getId() != null && returnedEvent.getId() != 0);



    }

}
