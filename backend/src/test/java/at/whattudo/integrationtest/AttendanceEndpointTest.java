package at.whattudo.integrationtest;

import at.whattudo.endpoint.AttendanceEndpoint;
import at.whattudo.endpoint.dto.StatusDto;
import at.whattudo.entity.*;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.LocationRepository;
import at.whattudo.repository.UserRepository;
import at.whattudo.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class AttendanceEndpointTest {
    @Autowired
    AttendanceEndpoint attendanceEndpoint;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    LocationRepository locationRepository;


    @WithMockUser(username = "testUser")
    @Test
    public void createAttendance_returnsCorrectAttendanceStatus_returnsCorrectUsers_returnsCorrectEvents() {
        Organization orga = new Organization("Test Organization");
        Calendar calendar = calendarRepository.save(new Calendar("Calendar test", Collections.singletonList(orga)));
        Location location = locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));
        Event event = eventRepository.save(new Event("Attend Event", LocalDateTime.of(2021, 8, 8, 15, 0), LocalDateTime.of(2021, 8, 8, 17, 0), calendar, location));
        ApplicationUser applicationUser = userRepository.save(new ApplicationUser("testUser", "testUser@testing.test", "testtest"));

        StatusDto statusDto = new StatusDto(applicationUser.getUsername(), event.getId(), AttendanceStatusPossibilities.ATTENDING);

        assertEquals(AttendanceStatusPossibilities.ATTENDING, attendanceEndpoint.create(statusDto).getStatus());
        assertEquals(event.getId(), attendanceEndpoint.getEventsUserIsAttending(applicationUser.getId()).get(0).getId());
        assertEquals(applicationUser.getId(), attendanceEndpoint.getUsersAttendingEvent(event.getId()).get(0).getId());

        statusDto.setStatus(AttendanceStatusPossibilities.INTERESTED);

        assertEquals(AttendanceStatusPossibilities.INTERESTED, attendanceEndpoint.create(statusDto).getStatus());
        assertEquals(Collections.emptyList(), attendanceEndpoint.getEventsUserIsAttending(applicationUser.getId()));
        assertEquals(event.getId(), attendanceEndpoint.getEventsUserIsInterested(applicationUser.getId()).get(0).getId());
        assertEquals(applicationUser.getId(), attendanceEndpoint.getUsersInterestedInEvent(event.getId()).get(0).getId());


        statusDto.setStatus(AttendanceStatusPossibilities.DECLINED);
        assertEquals(AttendanceStatusPossibilities.DECLINED, attendanceEndpoint.create(statusDto).getStatus());


        assertEquals(applicationUser.getId(), attendanceEndpoint.getUsersDecliningEvent(event.getId()).get(0).getId());
        assertEquals(Collections.emptyList(), attendanceEndpoint.getUsersInterestedInEvent(event.getId()));


    }
    @WithMockUser(username = "testUser 1")
    @Test
    public void deleteStatus(){
        Organization orga = new Organization("Test Organization");
        Calendar calendar = calendarRepository.save(new Calendar("Calendar test 1", Collections.singletonList(orga)));
        Event event = eventRepository.save(new Event("Attend Event 1", LocalDateTime.of(2021, 8, 8, 15, 0), LocalDateTime.of(2021, 8, 8, 17, 0), calendar));
        ApplicationUser applicationUser = userRepository.save(new ApplicationUser("testUser 1", "testUser1@testing.test", "testtest"));
        StatusDto statusDto = new StatusDto(applicationUser.getUsername(), event.getId(), AttendanceStatusPossibilities.ATTENDING);

        statusDto=  attendanceEndpoint.create(statusDto);

        attendanceEndpoint.deleteStatus(statusDto.getId());

        assertEquals(null, attendanceEndpoint.getStatus(applicationUser.getId(), event.getId()));
    }
    @WithMockUser(username = "testUser 3")
    @Test
    public void getStatus() {
        Organization orga = new Organization("Test Organization");
        Calendar calendar = calendarRepository.save(new Calendar("Calendar test 3", Collections.singletonList(orga)));
        Event event = eventRepository.save(new Event("Attend Event 3", LocalDateTime.of(2021, 8, 8, 15, 0), LocalDateTime.of(2021, 8, 8, 17, 0), calendar));
        ApplicationUser applicationUser = userRepository.save(new ApplicationUser("testUser 3", "testUser3@testing.test", "testtest"));
        StatusDto statusDto = new StatusDto(applicationUser.getUsername(), event.getId(), AttendanceStatusPossibilities.ATTENDING);
        statusDto = attendanceEndpoint.create(statusDto);
        assertEquals(AttendanceStatusPossibilities.ATTENDING, attendanceEndpoint.getStatus(applicationUser.getId(), event.getId()).getStatus());
    }
}
